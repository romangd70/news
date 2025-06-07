package com.example.news_aggregator.menu;

import lombok.Getter;

@Getter
public enum StaticMenu {
    MAIN("Агрегатор новостей"),
    READ_NEWS("Просмотр новостей"),
    STATISTICS("Отображение статистики"),
    SETTINGS("Настройки"),
    FILTER("Фильтровать новости");

    private final String title;

    StaticMenu(String title) {
        this.title = title;
    }
}
