package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsMenuBuilder;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class FilterNewsByKeywordSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final NewsMenuBuilder newsMenuBuilder;

    @Autowired
    protected FilterNewsByKeywordSwitcher(
            FilterService filterService,
            NewsMenuBuilder newsMenuBuilder
    ) {
        super(StaticMenuItem.BY_KEYWORDS_COMMAND);

        this.filterService = filterService;
        this.newsMenuBuilder = newsMenuBuilder;
    }

    @Override
    public void execute(Scanner scanner) {
        // TODO: Вывести список ключевых слов

        // TODO: Отфильтровать новости по ключевым словам
        List<News> filteredNews = Collections.emptyList();

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости по ключевому слову '%s'", "Указать ключевое слово");

        // Формируем динамическое меню для списка новостей и заменяем дескриптор перехода
        MenuDescriptor menuDescriptor = newsMenuBuilder.build(title, filteredNews);
        setNextMenuDescriptor(menuDescriptor);
    }
}
