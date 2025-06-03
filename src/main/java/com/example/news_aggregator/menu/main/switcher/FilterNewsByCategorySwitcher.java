package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.Category;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsMenuBuilder;
import com.example.news_aggregator.repository.CategoryRepository;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FilterNewsByCategorySwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final CategoryRepository categoryRepository;
    private final NewsMenuBuilder newsMenuBuilder;

    @Autowired
    protected FilterNewsByCategorySwitcher(
            FilterService filterService,
            CategoryRepository categoryRepository,
            NewsMenuBuilder newsMenuBuilder
    ) {
        super(StaticMenuItem.BY_CATEGORY_COMMAND);

        this.filterService = filterService;
        this.categoryRepository = categoryRepository;
        this.newsMenuBuilder = newsMenuBuilder;
    }

    @Override
    public void execute(Scanner scanner) {
        Map<Integer, Category> categoryById = categoryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Category::getId,
                        Function.identity()
                ));
        if (categoryById.isEmpty()) {
            System.out.println("Категории не найдены, добавьте новости.");
            return;
        }

        List<String> categoryNames = categoryById.values()
                .stream()
                .map(category -> String.format("%s - %s%n", category.getId(), category.getName()))
                .toList();

        StringBuilder stringBuilder = new StringBuilder();
        for (String category : categoryNames) {
            stringBuilder.append(category);
        }
        String allCategoriesAsString = stringBuilder.toString();

        System.out.println("Выберите из категорий. Введите нужный идентификатор \n" + allCategoriesAsString);

        // Получаем идентификатор категории из потока ввода
        Integer categoryId = Integer.parseInt(scanner.next());

        // Обработка неправильного ввода
        if (!categoryById.containsKey(categoryId)) {
            throw new NewsAggregatorIllegalArgumentException(Errors.UNKNOWN_MENU_KEY);
        }

        // Фильтрация новостей по категории
        FilterService.NewsFilter newsFilter = FilterService.NewsFilter.builder()
                .byCategory(categoryId)
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        Category category = categoryById.get(categoryId);
        String title = category.getName();

        // Формируем динамическое меню для списка новостей и заменяем дескриптор перехода
        MenuDescriptor menuDescriptor = newsMenuBuilder.build(title, filteredNews);
        setNextMenuDescriptor(menuDescriptor);
    }
}
