package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.MenuItem;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.MenuRegistryProvider;
import com.example.news_aggregator.enums.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MenuRegistryImpl implements MenuRegistry {

    private final Map<MenuDescriptor, Menu> menuById = new HashMap<>();
    private final Map<MenuItemDescriptor, MenuItem> menuItemById = new HashMap<>();

    private final Map<String, MenuDescriptor> menuDescriptorById = new HashMap<>();
    private final Map<String, MenuItemDescriptor> menuItemDescriptorById = new HashMap<>();

    @Autowired
    public MenuRegistryImpl(
            List<Menu> menus,
            List<MenuItem> menuItems,
            MenuRegistryProvider menuRegistryProvider
    ) {
        menus.forEach(this::registerMenu);
        menuItems.forEach(this::registerMenuItem);

        menuRegistryProvider.setMenuRegistry(this);
    }

    //region MenuItemRegistry implementation
    @Override
    public Menu getMenu(MenuDescriptor menuDescriptor) throws NewsAggregatorNotFoundException {
        // Первый поиск по предоставленному дескриптору
        if (!menuById.containsKey(menuDescriptor)) {
            // Извлечение ID дескриптора
            String menuDescriptorId = menuDescriptor.getId();
            if (!menuDescriptorById.containsKey(menuDescriptorId)) {
                throw new NewsAggregatorNotFoundException(
                        Errors.MENU_WITH_ID_S_TITLE_S_NOT_FOUND,
                        menuDescriptorId,
                        menuDescriptor.getTitle()
                );
            }
            // Получение зарегистрированного дескриптора по ID
            // По этому дескриптору меню гарантированно найдется
            menuDescriptor = menuDescriptorById.get(menuDescriptorId);
        }
        return menuById.get(menuDescriptor);
    }

    @Override
    public MenuItem getMenuItem(MenuItemDescriptor menuItemDescriptor) throws NewsAggregatorNotFoundException {
        // Первый поиск по предоставленному дескриптору
        if (!menuItemById.containsKey(menuItemDescriptor)) {
            // Извлечение ID дескриптора
            String menuItemDescriptorId = menuItemDescriptor.getId();
            if (!menuItemDescriptorById.containsKey(menuItemDescriptorId)) {
                throw new NewsAggregatorNotFoundException(
                        Errors.MENU_ITEM_WITH_ID_S_TITLE_S_NOT_FOUND,
                        menuItemDescriptorId,
                        menuItemDescriptor.getTitle()
                );
            }
            // Получение зарегистрированного дескриптора по ID
            // По этому дескриптору элемент меню гарантированно найдется
            menuItemDescriptor = menuItemDescriptorById.get(menuItemDescriptorId);
        }
        return menuItemById.get(menuItemDescriptor);
    }

    @Override
    public MenuDescriptor getMenuDescriptor(String menuDescriptorId) throws NewsAggregatorNotFoundException {
        if (!menuDescriptorById.containsKey(menuDescriptorId)) {
            throw new NewsAggregatorNotFoundException(
                    Errors.MENU_DESCRIPTOR_WITH_ID_S_NOT_FOUND,
                    menuItemDescriptorById
            );
        }
        return menuDescriptorById.get(menuDescriptorId);
    }

    @Override
    public MenuItemDescriptor getMenuItemDescriptor(String menuItemDescriptorId) {
        if (!menuItemDescriptorById.containsKey(menuItemDescriptorId)) {
            throw new NewsAggregatorNotFoundException(
                    Errors.MENU_ITEM_DESCRIPTOR_WITH_ID_S_NOT_FOUND,
                    menuItemDescriptorById
            );
        }
        return menuItemDescriptorById.get(menuItemDescriptorId);
    }

    @Override
    public void registerMenu(Menu menu) {
        menuById.put(menu.getDescriptor(), menu);
        menuDescriptorById.put(menu.getDescriptor().getId(), menu.getDescriptor());
    }

    @Override
    public void registerMenuItem(MenuItem menuItem) {
        menuItemById.put(menuItem.getDescriptor(), menuItem);
        menuItemDescriptorById.put(menuItem.getDescriptor().getId(), menuItem.getDescriptor());
    }

    @Override
    public void clear() {
        // Собираем списки динамических дескрипторов
        List<MenuDescriptor> menuDescriptorsToRemove = menuById.keySet().stream()
                .filter(MenuDescriptor::isDynamic)
                .toList();
        List<MenuItemDescriptor> menuItemDescriptorsToRemove = menuItemById.keySet().stream()
                .filter(MenuItemDescriptor::isDynamic)
                .toList();

        // Удаляем дескрипторы, меню и элементы меню
        menuDescriptorsToRemove.forEach(menuDescriptor -> {
            menuById.remove(menuDescriptor);
            menuDescriptorById.remove(menuDescriptor.getId());
        });
        menuItemDescriptorsToRemove.forEach(menuItemDescriptor -> {
            menuItemById.remove(menuItemDescriptor);
            menuItemDescriptorById.remove(menuItemDescriptor.getId());
        });
    }
    //endregion MenuItemRegistry implementation
}
