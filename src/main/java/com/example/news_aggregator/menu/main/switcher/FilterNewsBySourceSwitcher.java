package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.model.news.Source;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.repository.SourceRepository;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FilterNewsBySourceSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final SourceRepository sourceRepository;
    private final NewsDynamicMenuFactory newsDynamicMenuFactory;

    @Autowired
    protected FilterNewsBySourceSwitcher(
            FilterService filterService,
            SourceRepository sourceRepository,
            NewsDynamicMenuFactory newsDynamicMenuFactory
    ) {
        super(StaticMenuItem.BY_SOURCE_SWITCHER);

        this.filterService = filterService;
        this.sourceRepository = sourceRepository;
        this.newsDynamicMenuFactory = newsDynamicMenuFactory;
    }

    @Override
    public void execute(Scanner scanner) {
        Map<Integer, Source> sourcesById = sourceRepository.findAllByIsActive(true)
                .stream()
                .collect(Collectors.toMap(
                        Source::getId,
                        Function.identity()
                ));
        if (sourcesById.isEmpty()) {
            System.out.println("Источники не найдены, добавьте источники.");
            return;
        }
        System.out.println("Выберите из существующих источников. Введите id источника.");

        List<String> sourcesNames = sourcesById.values()
                .stream()
                .map(source -> String.format("%s - %s%n", source.getId(), source.getName()))
                .toList();

        StringBuilder stringBuilder = new StringBuilder();
        for (String source : sourcesNames) {
            stringBuilder.append(source);
        }
        String allSourcesAsString = stringBuilder.toString();

        System.out.println("Выберите из категорий. Введите нужный идентификатор \n" + allSourcesAsString);

        // Получаем идентификатор источника из потока ввода
        Integer sourceId = Integer.parseInt(scanner.next());

        // Обработка неправильного ввода
        if (!sourcesById.containsKey(sourceId)) {
            throw new NewsAggregatorIllegalArgumentException(Errors.UNKNOWN_MENU_KEY);
        }

        // Фильтрация источников по категории
        FilterService.NewsFilter newsFilter = FilterService.NewsFilter.builder()
                .bySource(sourceId)
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости от '%s'", sourcesById.get(sourceId).getName());

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        String menuId = newsDynamicMenuFactory.create(title, filteredNews);
        setNextMenuId(menuId);
    }
}