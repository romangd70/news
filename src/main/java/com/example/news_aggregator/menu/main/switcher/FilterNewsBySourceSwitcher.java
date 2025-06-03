package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsMenuBuilder;
import com.example.news_aggregator.repository.SourceRepository;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class FilterNewsBySourceSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final SourceRepository sourceRepository;
    private final NewsMenuBuilder newsMenuBuilder;

    @Autowired
    protected FilterNewsBySourceSwitcher(
            FilterService filterService,
            SourceRepository sourceRepository,
            NewsMenuBuilder newsMenuBuilder
    ) {
        super(StaticMenuItem.BY_SOURCE_COMMAND);

        this.filterService = filterService;
        this.sourceRepository = sourceRepository;
        this.newsMenuBuilder = newsMenuBuilder;
    }

    @Override
    public void execute(Scanner scanner) {
        sourceRepository.findAllByIsActive(true);
        System.out.println("Выберите из существующих источников. Введите id источника.");
        // TODO: Вывести список источников и организовать ввод, как в категориях, сделать общую функцию

        FilterService.NewsFilter newsFilter = FilterService.NewsFilter.builder()
                //.bySource(scanner.next())
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости от '%s'", "Указать источник");

        // Формируем динамическое меню для списка новостей и заменяем дескриптор перехода
        MenuDescriptor menuDescriptor = newsMenuBuilder.build(title, filteredNews);
        setNextMenuDescriptor(menuDescriptor);
    }
}