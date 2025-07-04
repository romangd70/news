package com.example.news_aggregator.common.menu;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;

import java.util.Scanner;

/**
 * Интерфейс команды.
 * Команда - элемент меню, который выполняет действие, но не осуществляет переход на другой уровень меню.
 */
public interface MenuCommand extends MenuItem {

    /**
     * Метод выполнения команды.
     *
     * @param scanner Объект сканера для обработки пользовательского ввода.
     * @throws NewsAggregatorIllegalArgumentException В случае, если пользователь ввел некорректное значение.
     *                                                Цикл обработки меню выведет сообщение об ошибке и отобразит заново текущий уровень меню.
     */
    void execute(Scanner scanner) throws NewsAggregatorIllegalArgumentException;
}