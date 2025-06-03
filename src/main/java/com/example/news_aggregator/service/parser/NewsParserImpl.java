package com.example.news_aggregator.service.parser;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalStateException;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.model.news.Category;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.model.news.Source;
import com.example.news_aggregator.repository.CategoryRepository;
import com.example.news_aggregator.repository.NewsRepository;
import com.example.news_aggregator.repository.SourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NewsParserImpl implements NewsParser {

    public static final String WITHOUT_CATEGORY = "Без категории";

    private final SourceRepository sourceRepository;
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public NewsParserImpl(SourceRepository sourceRepository,
                          NewsRepository newsRepository,
                          CategoryRepository categoryRepository) {
        this.sourceRepository = sourceRepository;
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Метод для вызова по расписанию. Парсит все источники новостей из базы данных.
     * Итогом работы метода является добавление новых статей из всех источников в базу данных.
     */
    @Override
    @Transactional
    public void parseAll() {
        List<Source> sources = sourceRepository.findAllByIsActive(true);
        for (Source source : sources) {
            parse(source);
        }
    }

    /**
     * Парсит переданный источник новостей
     * Итогом работы метода является добавление новых статей из переданного источника новостей в базу данных.
     * @param source - источник новостей для поиска статей
     */
    @Override
    public void parse(Source source) {
        String baseUrl = source.getNewsFeedUrl();

        try {
            // Переходит по url источника, выгружает в память html документ
            Document doc = Jsoup.connect(baseUrl).get();
            String cssArticleSelector = source.getArticleElementSelector();

            Elements articlesFromSource = null;

            // Выбираем из полученного html документа список статей по указанным в базе данных селекторам и аттрибутам
            if (cssArticleSelector != null && !cssArticleSelector.isEmpty()) {
                articlesFromSource = doc.select(source.getArticleElementSelector());
            }

            // Если статьи не найдены по указанным селекторам и аттрибутам
            if (articlesFromSource == null || articlesFromSource.isEmpty()) {
                throw new NewsAggregatorIllegalStateException(Errors.NO_ARTICLES_FOUND_FOR_SOURCE_NAME_S, source.getName());
            }

            List<News> newsList = new ArrayList<>();
            // Парсим каждую статью
            for (Element link : articlesFromSource) {
                String articleUrl = link.absUrl("href");
                newsList.add(parseArticle(articleUrl, source));
            }

            // Исключаем статьи, которые уже есть в базе данных
            List<News> repeatingNews = newsRepository.findByHashIn(newsList.stream().map(News::getHash).toList());
            newsList.removeAll(repeatingNews);

            // Сохраняем новости
            newsRepository.saveAll(newsList);

        } catch (IOException e) {
            // При ошибке парсинга
            throw new NewsAggregatorIllegalStateException(Errors.UNABLE_TO_CONNECT_SOURCE_S, source.getName());
        }
    }

    /**
     * Парсит статью по переданному на нее url.
     * Итогом работы метода является добавление новой статьи из переданного url в базу данных.
     * @param articleUrl - url статьи для парсинга
     * @param source - источник статьи
     */
    private News parseArticle(String articleUrl, Source source) throws NewsAggregatorIllegalStateException{
        try {
            Document articleDoc = Jsoup.connect(articleUrl).get();

            News news = parseMetadata(articleDoc, source);
            parseMediaLinks(articleDoc);
            news.setMediaLinks(Collections.emptyList());
            news.setHash(news.hashCode());
            return news;
        } catch (IOException e) {
            throw new NewsAggregatorIllegalStateException(Errors.ERROR_PARSING_ARTICLE_S);
        }
    }

    private News parseMetadata(Document articleDoc, Source source) {
        // Cобираем селекторы и аттрибуты из базы данных, чтобы выделить нужную информацию из статьи
        String titleSelector = source.getTitleSelector();
        String titleAttribute = source.getTitleAttribute();
        String publicationDateTimeSelector = source.getPublishedAtSelector();
        String publicationDateTimeAttribute = source.getPublishedAtAttribute();
        String descriptionSelector = source.getShortDescriptionSelector();
        String descriptionAttribute = source.getShortDescriptionAttribute();
        String contentSelector = source.getContentSelector();
        String contentAttribute = source.getContentAttribute();
        String keywordSelector = source.getKeywordSelector();
        String keywordAttribute = source.getKeywordAttribute();
        String categorySelector = source.getCategorySelector();
        String categoryAttribute = source.getCategoryAttribute();

        String title = parseWithAttribute(articleDoc, titleSelector, titleAttribute);
        String publicationDate = parseWithAttribute(articleDoc, publicationDateTimeSelector, publicationDateTimeAttribute);
        String description = parseWithAttribute(articleDoc, descriptionSelector, descriptionAttribute);
        String content = parseWithAttribute(articleDoc, contentSelector, contentAttribute);
        String[] keywords = parseWithAttribute(articleDoc, keywordSelector, keywordAttribute)
                .toLowerCase()
                .split(",");
        keywords = Arrays.stream(keywords)
                .map(String::trim)
                .toArray(String[]::new);

        String category = parseWithAttribute(articleDoc, categorySelector, categoryAttribute);
        if (category.isEmpty()) {
            category = WITHOUT_CATEGORY;
        }
        // Форматируем дату для сохранения
        LocalDateTime formattedPublicationDateTime = formatDateTime(source.getDateTimeFormat(), publicationDate);
        List<String> keywordsAsList = Arrays.stream(keywords).toList();

        Category categoryToSet = findOrCreateCategory(category);

        // Вернуть результат парсинга как объект новости
        return new News(
                null, // нет идентификатора при создании
                title,
                description,
                content,
                articleDoc.html(),
                formattedPublicationDateTime,
                source,
                categoryToSet,
                keywordsAsList,
                null, // нет ссылок на данный момент
                null); // нет hash
    }

    private List<String> parseMediaLinks(Document articleDoc) {
        Elements mediaLinks = articleDoc.select("img");
        for (Element media : mediaLinks) {
            String mediaUrl = media.absUrl("src");
            System.out.println("Медиафайл: " + mediaUrl);
        }
        return Collections.emptyList();
    }

    private String parseWithAttribute(Document articleDoc, String selector, @Nullable String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return parseWithoutAttribute(articleDoc, selector);
        }
        return articleDoc.select(selector).attr(attribute);
    }

    private String parseWithoutAttribute(Document articleDoc, String selector) {
        return articleDoc.select(selector).text();
    }

    private LocalDateTime formatDateTime(String format, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            log.error("Ошибка разбора даты и времени [{}]. {}.", date, e.getMessage());
            return LocalDateTime.now();
        }
    }

    private Category findOrCreateCategory(String categoryName) {
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (category.isPresent()) {
            return category.get();
        } else {
            return categoryRepository.save(new Category(null, categoryName));
        }
    }
}
