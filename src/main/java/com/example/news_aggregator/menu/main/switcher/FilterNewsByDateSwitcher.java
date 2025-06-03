package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsMenuBuilder;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class FilterNewsByDateSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final NewsMenuBuilder newsMenuBuilder;

    @Autowired
    protected FilterNewsByDateSwitcher(
            FilterService filterService,
            NewsMenuBuilder newsMenuBuilder
    ) {
        super(StaticMenuItem.BY_DATE_COMMAND);

        this.filterService = filterService;
        this.newsMenuBuilder = newsMenuBuilder;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите дату в формате dd-MM-yyyy");

        FilterService.NewsFilter newsFilter = FilterService.NewsFilter.builder()
                .byDate(scanner.next())
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости на %s", newsFilter.getByDate());

        // Формируем динамическое меню для списка новостей и заменяем дескриптор перехода
        MenuDescriptor menuDescriptor = newsMenuBuilder.build(title, filteredNews);
        setNextMenuDescriptor(menuDescriptor);
    }
}
