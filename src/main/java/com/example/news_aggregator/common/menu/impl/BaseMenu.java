package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuItem;
import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовая реализация меню.
 * Включает частичную реализацию интерфейса Menu посредством аннотации @Getter из Lombok.
 */
@Getter
public class BaseMenu implements Menu {

    /**
     * Настройка для метода printWrapped.
     * Текст будет перенесен на новую строку, если длина строки превысит заданное значение.
     */
    protected final static int DEFAULT_LINE_WIDTH = 80;

    private final MenuRegistry menuRegistry;

    private final String id;
    private final String title;
    private final boolean isDynamic;

    private final Map<String, String> menuItemIdByInputKey = new HashMap<>();
    private final List<MenuItemWithKey> menuItemIdWithKeys = new ArrayList<>();

    /**
     * Конструирование меню из статического описания.
     *
     * @param staticMenu Элемент перечисления статических меню.
     */
    protected BaseMenu(
            StaticMenu staticMenu,
            MenuRegistry menuRegistry
    ) {
        this(
                staticMenu.name(), // ID меню - уникальное имя элемента перечисления
                staticMenu.getTitle(), // Заголовок меню
                false, // Флаг происхождения - false - статическое меню
                menuRegistry // Реестр меню
        );
    }

    /**
     * Конструирование динамического меню.
     *
     * @param id           ID меню.
     * @param title        Заголовок меню.
     * @param isDynamic    Флаг происхождения - false - статическое меню, true - динамическое меню.
     * @param menuRegistry Реестр меню.
     */
    public BaseMenu(
            String id,
            String title,
            boolean isDynamic,
            MenuRegistry menuRegistry
    ) {
        this.id = id;
        this.title = title;
        this.isDynamic = isDynamic;
        this.menuRegistry = menuRegistry;
    }

    //region Menu implementation
    @Override
    public MenuItem getMenuItem(String inputKey) throws NewsAggregatorNotFoundException {
        String menuItemId;
        // Сначала получим идентификатор элемента меню по ключу ввода
        if (menuItemIdByInputKey.containsKey(inputKey)) {
            menuItemId = menuItemIdByInputKey.get(inputKey);
        } else {
            throw new NewsAggregatorNotFoundException(Errors.UNKNOWN_MENU_KEY);
        }

        // Теперь запросим элемент меню в реестре
        return menuRegistry.getMenuItem(menuItemId);
    }

    @Override
    public void print() {
        printMenuHeader();
        printMenuDescription();
        printMenuItems();
        printMenuFooter();
    }
    //endregion Menu implementation

    /**
     * Добавляет элемент статического меню.
     *
     * @param inputKey       Ключ активации элемента меню (вводится с клавиатуры).
     * @param staticMenuItem Элемент перечисления, описывающего элемент меню.
     */
    public void addMenuItem(String inputKey, StaticMenuItem staticMenuItem) {
        String menuItemId = staticMenuItem.name();
        addMenuItem(inputKey, menuItemId);
    }

    /**
     * Добавляет элемент динамического меню.
     *
     * @param inputKey   Ключ активации элемента меню (вводится с клавиатуры).
     * @param menuItemId Идентификатор элемент меню.
     */
    public void addMenuItem(String inputKey, String menuItemId) {
        menuItemIdByInputKey.put(inputKey, menuItemId);
        menuItemIdWithKeys.add(new MenuItemWithKey(inputKey, menuItemId));
    }

    //region Protected helpers

    /**
     * Выводит на экран заголовок меню.
     */
    protected void printMenuHeader() {
        System.out.printf("%n=== %s ===%n", getTitle());
    }

    /**
     * Выводит на экран описание меню.
     */
    protected void printMenuDescription() {
        // В базовой реализации ничего не выводим.
        // Наследники могут переопределить метод и вывести необходимую информацию.
    }

    /**
     * Выводит на экран список элементов меню.
     */
    protected void printMenuItems() {
        int maxInputKeyLength = 0;
        for (MenuItemWithKey menuItemWithKey : menuItemIdWithKeys) {
            maxInputKeyLength = Math.max(maxInputKeyLength, menuItemWithKey.inputKey.length());
        }
        String format = String.format(
                "[%%%ss]: %%s%%n",
                maxInputKeyLength
        );

        for (MenuItemWithKey menuItemWithKey : menuItemIdWithKeys) {
            String inputKey = menuItemWithKey.inputKey();
            MenuItem menuItem = getMenuItem(inputKey);
            System.out.printf(format, inputKey, menuItem.getTitle());
        }
    }

    /**
     * Выводит на экран нижнее обрамление меню.
     */
    protected void printMenuFooter() {
        System.out.println("==========================");
    }

    /**
     * Вывод текста с заданной шириной в консоль с переносом в границах слов.
     *
     * @param text      Текст для вывода.
     * @param lineWidth Желаемая ширина строки в символах.
     */
    protected void printWrapped(String text, int lineWidth) {
        // Разбивка текста по словам
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        // Формирование строки из слов
        for (String word : words) {
            // Длина строки превысит граничную, если добавить еще слово
            if (line.length() + word.length() > lineWidth) {
                // Вывод очередной строки
                System.out.println(line);
                // Создание билдера для следующей строки
                line = new StringBuilder(word);
            } else {
                // Добавим слово в билдер строки, не забывая про пробел между словами
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            }
        }

        // Вывод последней строки
        if (!line.isEmpty()) {
            System.out.println(line);
        }
    }
    //endregion Protected helpers

    /**
     * Кортеж из двух элементов, который сопоставляет ключ ввода идентификатору элемента меню.
     *
     * @param inputKey   Ключ ввода.
     * @param menuItemId Идентификатор элемента меню.
     */
    private record MenuItemWithKey(String inputKey, String menuItemId) {
    }
}
