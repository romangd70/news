package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.service.filter.FilterService;
import com.example.news_aggregator.service.filter.NewsFilter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class FilterNewsByDateSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory;

    @Autowired
    protected FilterNewsByDateSwitcher(
            FilterService filterService,
            ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory
    ) {
        super(StaticMenuItem.BY_DATE_SWITCHER);

        this.filterService = filterService;
        this.newsDynamicMenuFactoryObjectFactory = newsDynamicMenuFactoryObjectFactory;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите дату в формате dd-MM-yyyy: ");
        String inputDate = readLine(scanner);

        NewsFilter newsFilter = NewsFilter.builder()
                .byDate(inputDate)
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости на %s", newsFilter.getByDate());

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        NewsDynamicMenuFactory newsDynamicMenuFactory = newsDynamicMenuFactoryObjectFactory.getObject();
        String menuId = newsDynamicMenuFactory.create(title, filteredNews);
        setNextMenuId(menuId);
    }
}
