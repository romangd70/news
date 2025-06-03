package com.example.news_aggregator.menu;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.impl.MenuDescriptorImpl;
import lombok.Getter;

@Getter
public enum StaticMenu {
    MAIN("Агрегатор новостей"),
    READ_NEWS("Просмотр новостей"),
    STATISTICS("Отображение статистики"),
    FILTER("Фильтровать новости");

    private final String title;

    StaticMenu(String title) {
        this.title = title;
    }

    public static MenuDescriptor toDescriptor(StaticMenu staticMenu) {
        return new MenuDescriptorImpl(
                staticMenu.name(),
                staticMenu.getTitle(),
                false // Статический дескриптор
        );
    }
}
