package com.example.news_aggregator.common.menu;

/**
 * Маркерный интерфейс для цикла обработки меню.
 * Позволяет определить, что выбранная команда должна завершить приложение.
 * Наследование от MenuCommand позволяет реализовать действия перед завершением работы.
 */
public interface TerminateApplicationCommand extends MenuCommand {
}