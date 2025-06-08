package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.MenuUtils;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.news.Category;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.repository.CategoryRepository;
import com.example.news_aggregator.service.filter.FilterService;
import org.springframework.beans.factory.ObjectFactory;
import com.example.news_aggregator.service.filter.NewsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FilterNewsByCategorySwitcher extends BaseMenuSwitcher {

    private final FilterService filterService;
    private final CategoryRepository categoryRepository;
    private final ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory;

    @Autowired
    protected FilterNewsByCategorySwitcher(
            FilterService filterService,
            CategoryRepository categoryRepository,
            ObjectFactory<NewsDynamicMenuFactory> newsDynamicMenuFactoryObjectFactory
    ) {
        super(StaticMenuItem.BY_CATEGORY_SWITCHER);

        this.filterService = filterService;
        this.categoryRepository = categoryRepository;
        this.newsDynamicMenuFactoryObjectFactory = newsDynamicMenuFactoryObjectFactory;
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

        // Подготавливаем список категорий к отображению
        // Ключом ввода выступит идентификатор категории
        List<MenuUtils.MenuItemToDisplay> menuItemsToDisplay = categoryById.values()
                .stream()
                .map(category -> new MenuUtils.MenuItemToDisplay(
                        String.valueOf(category.getId()), category.getName()))
                .sorted(Comparator.comparing(MenuUtils.MenuItemToDisplay::getMenuItemTitle))
                .toList();

        // Отображаем список и получаем ключ из потока ввода
        String inputKey = performInput(
                scanner,
                "Категории новостей",
                "Введите идентификатор категории: ",
                menuItemsToDisplay
        );

        // Преобразуем ключ ввода в идентификатор
        int categoryId = Integer.parseInt(inputKey);

        // Фильтрация новостей по категории
        NewsFilter newsFilter = NewsFilter.builder()
                .byCategory(categoryId)
                .build();
        List<News> filteredNews = filterService.filter(newsFilter);

        // Формируем заголовок для отображения меню просмотра
        Category category = categoryById.get(categoryId);
        String title = category.getName();

        // Формируем динамическое меню для списка новостей и заменяем идентификатор перехода
        NewsDynamicMenuFactory newsDynamicMenuFactory = newsDynamicMenuFactoryObjectFactory.getObject();
        String menuId = newsDynamicMenuFactory.create(title, filteredNews);
        setNextMenuId(menuId);    }
}
