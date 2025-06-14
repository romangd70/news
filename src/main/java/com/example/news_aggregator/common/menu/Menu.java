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
     * Получение идентификатора элемента меню по ключу ввода.
     * Элемент меню можно получить в реестре меню, используя этот идентификатор.
     *
     * @param inputKey Ключ ввода для активации элемента меню.
     * @return Уникальный идентификатор элемента меню.
     * @throws NewsAggregatorNotFoundException Если с ключом ввода не связан ни один элемент меню.
     */
    String getMenuItemId(String inputKey) throws NewsAggregatorNotFoundException;

    /**
     * Метод для визуализации меню.
     */
    void print();
}
