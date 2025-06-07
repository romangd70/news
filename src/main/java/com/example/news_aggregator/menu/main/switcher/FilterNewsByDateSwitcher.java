package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class FilterNewsByDateSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final NewsDynamicMenuFactory newsDynamicMenuFactory;

    @Autowired
    protected FilterNewsByDateSwitcher(
            FilterService filterService,
            NewsDynamicMenuFactory newsDynamicMenuFactory
    ) {
        super(StaticMenuItem.BY_DATE_SWITCHER);

        this.filterService = filterService;
        this.newsDynamicMenuFactory = newsDynamicMenuFactory;
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

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        String menuId = newsDynamicMenuFactory.create(title, filteredNews);
        setNextMenuId(menuId);
    }
}
