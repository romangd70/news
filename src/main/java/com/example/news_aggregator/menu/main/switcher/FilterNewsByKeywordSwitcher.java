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
public class FilterNewsByKeywordSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory;

    @Autowired
    protected FilterNewsByKeywordSwitcher(
            FilterService filterService,
            ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory
    ) {
        super(StaticMenuItem.BY_KEYWORDS_SWITCHER);

        this.filterService = filterService;
        this.newsDynamicMenuFactoryObjectFactory = newsDynamicMenuFactoryObjectFactory;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите ключевое слово: ");
        String keyword = readLine(scanner);

        List<News> filteredNews = filterService.filter(NewsFilter
                .builder()
                .byKeyword(keyword)
                .build());

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости по ключевому слову '%s'", keyword);

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        NewsDynamicMenuFactory newsDynamicMenuFactory = newsDynamicMenuFactoryObjectFactory.getObject();
        String menuId = newsDynamicMenuFactory.create(title, filteredNews);
        setNextMenuId(menuId);
    }
}
