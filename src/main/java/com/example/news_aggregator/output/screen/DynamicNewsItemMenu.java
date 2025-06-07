package com.example.news_aggregator.output.screen;

import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.model.news.News;

/**
 * Реализация динамического меню для просмотра содержимого объекта новости.
 * Выполнение операций над объектом новости.
 */
public class DynamicNewsItemMenu extends BaseMenu {

    private final News newsItem;

    /**
     * Конструирование динамического меню просмотра новости.
     *
     * @param newsItem Объект новости к отображению.
     */
    DynamicNewsItemMenu(
            String id,
            String title,
            MenuRegistry menuRegistry,
            News newsItem
    ) {
        super(id, title, true, menuRegistry);

        this.newsItem = newsItem;
    }

    @Override
    protected void printMenuDescription() {
        // Выведем информацию о новости текст новости в описание меню
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(String.format("%-15s: %s%n", "Дата публикации", newsItem.getPublicationDate()));
        headerBuilder.append(String.format("%-15s: %s%n", "Категория", newsItem.getCategory().getName()));
        headerBuilder.append(String.format("%-15s: %s%n", "Источник", newsItem.getSource().getName()));
        System.out.println(headerBuilder);

        System.out.println(newsItem.getTitle());
        System.out.println();

        printWrapped(newsItem.getContent(), BaseMenu.DEFAULT_LINE_WIDTH);
        System.out.println(newsItem.getUrl());
        System.out.println();
    }
}
