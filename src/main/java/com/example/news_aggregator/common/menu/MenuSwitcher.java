package com.example.news_aggregator.common.menu;

/**
 * Интерфейс переключателя.
 * Переключатель это команда, которая после выполнения действия произведет переключение контекста ввода на другое меню.
 */
public interface MenuSwitcher extends MenuCommand {

    /**
     * Получение идентификатора меню, переход на которое должен быть осуществлен.
     *
     * @return Строка, содержащая идентификатор меню.
     */
    String getNextMenuId();
}