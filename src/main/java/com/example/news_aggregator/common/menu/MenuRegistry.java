package com.example.news_aggregator.common.menu;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;

/**
 * Интерфейс реестра меню.
 */
public interface MenuRegistry {

    /**
     * Получение меню по идентификатору.
     *
     * @param menuId Идентификатор меню.
     * @return Объект меню.
     * @throws NewsAggregatorNotFoundException Если меню не может быть найдено по указанному идентификатору.
     */
    Menu getMenu(String menuId) throws NewsAggregatorNotFoundException;

    /**
     * Получение элемента меню по идентификатору.
     *
     * @param menuItemId Идентификатор элемента меню.
     * @return Объект элемента меню
     * @throws NewsAggregatorNotFoundException Если элемент меню не может быть найдено по указанному идентификатору.
     */
    MenuItem getMenuItem(String menuItemId) throws NewsAggregatorNotFoundException;

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
