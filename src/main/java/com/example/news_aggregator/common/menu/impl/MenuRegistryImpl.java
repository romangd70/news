package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuItem;
import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.enums.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация реестра меню.
 * Статические компоненты меню будут собраны автоматически.
 * Динамические компоненты необходимо регистрировать вручную.
 */
@Component
public class MenuRegistryImpl implements MenuRegistry {

    private final Map<String, Menu> menuById = new HashMap<>();
    private final Map<String, MenuItem> menuItemById = new HashMap<>();

    @Autowired
    protected MenuRegistryImpl(
            List<Menu> menus,
            List<MenuItem> menuItems
    ) {
        menus.forEach(this::registerMenu);
        menuItems.forEach(this::registerMenuItem);
        System.out.println("<*> Регистрация статических меню успешно выполнена!");
    }

    //region MenuItemRegistry implementation
    @Override
    public Menu getMenu(String menuId) throws NewsAggregatorNotFoundException {
        if (!menuById.containsKey(menuId)) {
            throw new NewsAggregatorNotFoundException(Errors.MENU_WITH_ID_S_NOT_FOUND, menuId);
        }
        return menuById.get(menuId);
    }

    @Override
    public MenuItem getMenuItem(String menuItemId) throws NewsAggregatorNotFoundException {
        if (!menuItemById.containsKey(menuItemId)) {
            throw new NewsAggregatorNotFoundException(Errors.MENU_ITEM_WITH_ID_S_NOT_FOUND, menuItemId);
        }
        return menuItemById.get(menuItemId);
    }

    @Override
    public void registerMenu(Menu menu) {
        menuById.put(menu.getId(), menu);
    }

    @Override
    public void registerMenuItem(MenuItem menuItem) {
        menuItemById.put(menuItem.getId(), menuItem);
    }

    @Override
    public void clear() {
        // Собираем списки идентификаторов динамических элементов меню
        List<String> menuIdsToRemove = menuById.values().stream()
                .filter(Menu::isDynamic)
                .map(Menu::getId)
                .toList();
        List<String> menuItemIdsToRemove = menuItemById.values().stream()
                .filter(MenuItem::isDynamic)
                .map(MenuItem::getId)
                .toList();

        // Удаляем меню и элементы меню, используя полученные идентификаторы
        menuIdsToRemove.forEach(menuById::remove);
        menuItemIdsToRemove.forEach(menuItemById::remove);
    }
    //endregion MenuItemRegistry implementation
}
