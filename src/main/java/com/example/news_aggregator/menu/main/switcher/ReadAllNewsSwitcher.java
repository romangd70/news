package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ReadAllNewsSwitcher extends BaseMenuSwitcher {

    private final NewsRepository newsRepository;
    private final NewsDynamicMenuFactory newsDynamicMenuFactory;

    @Autowired
    protected ReadAllNewsSwitcher(
            NewsRepository newsRepository,
            NewsDynamicMenuFactory newsDynamicMenuFactory
    ) {
        super(StaticMenuItem.ALL_NEWS_SWITCHER);

        this.newsRepository = newsRepository;
        this.newsDynamicMenuFactory = newsDynamicMenuFactory;
    }

    @Override
    public void execute(Scanner scanner) {
        List<News> allNews = newsRepository.findAll();

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        String menuId = newsDynamicMenuFactory.create("Все новости", allNews);
        setNextMenuId(menuId);
    }
}
