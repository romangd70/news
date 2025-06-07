package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuCommand;
import com.example.news_aggregator.menu.StaticMenuItem;
import lombok.Getter;

import java.util.Scanner;

/**
 * Базовая реализация команды меню.
 * Включает реализацию интерфейса MenuItem посредством аннотации @Getter из Lombok.
 */
@Getter
public class BaseMenuCommand implements MenuCommand {

    private final String id;
    private final String title;
    private final boolean isDynamic;

    /**
     * Конструирование элемента меню из статического описания.
     *
     * @param staticMenuItem Элемент перечисления статических элементов меню.
     */
    protected BaseMenuCommand(
            StaticMenuItem staticMenuItem
    ) {
        this(
                staticMenuItem.name(), // ID элемента меню - уникальное имя элемента перечисления
                staticMenuItem.getTitle(), // Наименование элемента меню
                false // Флаг происхождения - false - статическое меню
        );
    }

    /**
     * Конструирование динамического элемента меню.
     *
     * @param id        ID элемента меню.
     * @param title     Наименование элемента меню.
     * @param isDynamic Флаг происхождения - false - статическое меню, true - динамическое меню.
     */
    protected BaseMenuCommand(
            String id,
            String title,
            boolean isDynamic
    ) {
        this.id = id;
        this.title = title;
        this.isDynamic = isDynamic;
    }

    @Override
    public void execute(Scanner scanner) {
        // Базовая реализация не требуется
    }
}
