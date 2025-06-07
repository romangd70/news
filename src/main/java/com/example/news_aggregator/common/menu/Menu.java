package com.example.news_aggregator.common.menu;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;

/**
 * Интерфейс меню.
 */
public interface Menu {

    /**
     * Получение уникального идентификатора меню.
     *
     * @return Строка, содержащая идентификатор меню.
     */
    String getId();

    /**
     * Получение заголовка меню.
     *
     * @return Строка, содержащая заголовок меню.
     */
    String getTitle();

    /**
     * Флаг, указывающий на происхождение меню.
     * Меню может быть либо статическим, либо динамическим.
     * Анализ значения флага возлагается на объект реестра меню для очистки системы от динамически созданных меню.
     *
     * @return True - меню создано динамически, False - меню является статическим.
     */
    boolean isDynamic();

    /**
     * Получение элемента меню.
     *
     * @param inputKey Ключ ввода для активации элемента меню.
     * @return Объект элемента меню
     * @throws NewsAggregatorNotFoundException Если с ключом ввода не связан ни один элемент меню.
     */
    MenuItem getMenuItem(String inputKey) throws NewsAggregatorNotFoundException;

    /**
     * Метод для визуализации меню.
     */
    void print();
}
