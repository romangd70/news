package com.example.news_aggregator.common.menu;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;

public interface MenuRegistry {

    Menu getMenu(MenuDescriptor menuDescriptor) throws NewsAggregatorNotFoundException;

    MenuItem getMenuItem(MenuItemDescriptor menuItemDescriptor) throws NewsAggregatorNotFoundException;

    MenuDescriptor getMenuDescriptor(String menuDescriptorId);

    MenuItemDescriptor getMenuItemDescriptor(String menuItemDescriptorId);

    /**
     * Регистрация динамического меню.
     *
     * @param menu Объект меню, подлежащий регистрации.
     */
    void registerMenu(Menu menu);

    /**
     * Регистрация динамического элемента меню.
     *
     * @param menuItem Объект элемента меню, подлежащий регистрации.
     */
    void registerMenuItem(MenuItem menuItem);

    /**
     * Очищает реестр от динамически созданных меню.
     */
    void clear();
}
