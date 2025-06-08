package com.example.news_aggregator.common.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class MenuUtils {

    private MenuUtils() {
        // Скрываем конструктор класса, мы не собираемся создавать экземпляры
    }

    /**
     * Возвращает строку для форматирования заголовка элемента меню с ключом ввода.
     * Форматирование ожидает два параметра:
     * - ключ ввода
     * - наименование элемента меню
     *
     * @return Строка форматирования.
     */
    public static String createMenuItemTitleFormat(
            List<String> inputKeys
    ) {
        // Для выравнивания вывода ключей ввода по ширине создадим форматку с фиксированием длинны
        int maxInputKeyLength = 0;
        for (String inputKey : inputKeys) {
            maxInputKeyLength = Math.max(maxInputKeyLength, inputKey.length());
        }
        return String.format("[%%%ss]: %%s%%n", maxInputKeyLength);
    }

    public static void displayMenuItems(
            List<MenuItemToDisplay> menuItems
    ) {
        // Для выравнивания вывода ключей ввода по ширине создадим форматку с фиксированием длинны
        // Соберем все возможные ключи
        List<String> inputKeys = menuItems
                .stream()
                .map(MenuItemToDisplay::getInputKey)
                .toList();
        String format = MenuUtils.createMenuItemTitleFormat(inputKeys);

        // Вывод наименования элемента меню с использованием динамически полученной форматки
        menuItems.forEach(menuItemData -> System.out.printf(
                format,
                menuItemData.getInputKey(),
                menuItemData.getMenuItemTitle())
        );
    }

    /**
     * Данные для вывода элемента меню на экран.
     */
    @Getter
    @AllArgsConstructor
    public static class MenuItemToDisplay {
        private final String inputKey;
        private final String menuItemTitle;
    }
}
