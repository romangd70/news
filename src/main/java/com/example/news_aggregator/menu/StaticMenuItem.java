package com.example.news_aggregator.menu;

import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.common.menu.impl.MenuItemDescriptorImpl;
import lombok.Getter;

@Getter
public enum StaticMenuItem {
    MAIN_MENU_SWITCHER("Главное меню"), // Переход в главное меню
    PARSE_NEWS_COMMAND("Парсинг новостей"), // Команда запуска парсинга
    READ_NEWS_SWITCHER("Просмотр новостей"), // Переход в меню просмотра новостей
    DISPLAY_STATISTICS_SWITCHER("Отображение статистики"), // Переход в меню отображения статистики

    // Просмотр новостей
    ALL_NEWS_SWITCHER("Все новости"), // Просмотр новостей -> Все Новости
    FILTER_NEWS_SWITCHER("Фильтровать новости"), // Просмотр новостей -> Фильтровать новости
    BY_DATE_COMMAND("По дате"),// Просмотр новостей -> Фильтровать новости -> По дате
    BY_SOURCE_COMMAND("По источнику"), // Просмотр новостей -> Фильтровать новости -> По источнику
    BY_CATEGORY_COMMAND("По категории"), // Просмотр новостей -> Фильтровать новости -> По категории
    BY_KEYWORDS_COMMAND("По ключевым словам"), // Просмотр новостей -> Фильтровать новости -> По ключевым словам

    // Отображение статистики
    BY_CATEGORY_STATISTICS("Статистика количества новостей по категориям"), // Отображение статистики -> Статистика количества новостей по категориям
    TOP_THEMES_STATISTICS("Топ упоминаемости тем и персон"), // Отображение статистики -> Топ упоминаемости тем
    DYNAMIC_POPULARITY_STATISTICS("Статистика динамики роста популярности тем"), // Отображение статистики -> Динамическая статистика роста популярности тем

    APPLICATION_EXIT_COMMAND("Выход из программы"); // Команда выхода из приложения

    private final String title;

    StaticMenuItem(String title) {
        this.title = title;
    }

    public static MenuItemDescriptor toDescriptor(StaticMenuItem staticMenuItem) {
        return new MenuItemDescriptorImpl(
                staticMenuItem.name(),
                staticMenuItem.getTitle(),
                false // Статический дескриптор
        );
    }
}