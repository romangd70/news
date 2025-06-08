package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.MenuUtils;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.model.news.Source;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.repository.SourceRepository;
import com.example.news_aggregator.service.filter.FilterService;
import com.example.news_aggregator.service.filter.NewsFilter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FilterNewsBySourceSwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final SourceRepository sourceRepository;
    private final ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory;

    @Autowired
    protected FilterNewsBySourceSwitcher(
            FilterService filterService,
            SourceRepository sourceRepository,
            ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory
    ) {
        super(StaticMenuItem.BY_SOURCE_SWITCHER);

        this.filterService = filterService;
        this.sourceRepository = sourceRepository;
        this.newsDynamicMenuFactoryObjectFactory = newsDynamicMenuFactoryObjectFactory;
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

        // Подготавливаем список источников к отображению
        // Ключом ввода выступит идентификатор источника
        List<MenuUtils.MenuItemToDisplay> menuItemsToDisplay = sourcesById.values()
                .stream()
                .map(source -> new MenuUtils.MenuItemToDisplay(
                        String.valueOf(source.getId()), source.getName()))
                .sorted(Comparator.comparing(MenuUtils.MenuItemToDisplay::getMenuItemTitle))
                .toList();

        // Отображаем список и получаем ключ из потока ввода
        String inputKey = performInput(
                scanner,
                "Доступные источники",
                "Введите идентификатор источника: ",
                menuItemsToDisplay
        );

        // Преобразуем ключ ввода в идентификатор
        int sourceId = Integer.parseInt(inputKey);

        // Фильтрация источников по источникам
        NewsFilter newsFilter = NewsFilter.builder()
                .bySource(sourceId)
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        String title = String.format("Новости от '%s'", sourcesById.get(sourceId).getName());

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        NewsDynamicMenuFactory newsDynamicMenuFactory = newsDynamicMenuFactoryObjectFactory.getObject();
        String menuId = newsDynamicMenuFactory.create(title, filteredNews);
        setNextMenuId(menuId);
    }
}