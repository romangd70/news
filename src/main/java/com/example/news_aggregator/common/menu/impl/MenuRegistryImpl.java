package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuItem;
import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.enums.Errors;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация реестра меню.
 */
@Component
public class MenuRegistryImpl implements MenuRegistry {

    private final Map<String, Menu> menuById = new HashMap<>();
    private final Map<String, MenuItem> menuItemById = new HashMap<>();

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
        // Собираем списки динамических дескрипторов
        List<String> menuIdsToRemove = menuById.values().stream()
                .filter(Menu::isDynamic)
                .map(Menu::getId)
                .toList();
        List<String> menuItemIdsToRemove = menuItemById.values().stream()
                .filter(MenuItem::isDynamic)
                .map(MenuItem::getId)
                .toList();

        // Удаляем меню и элементы меню
        menuIdsToRemove.forEach(menuById::remove);
        menuItemIdsToRemove.forEach(menuItemById::remove);
    }
    //endregion MenuItemRegistry implementation
}
