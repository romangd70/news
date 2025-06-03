package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseMenu implements Menu {

    protected final static int LINE_WIDTH = 80;

    private final Map<String, MenuItemDescriptor> menuItemDescriptorByKey = new HashMap<>();
    private final List<MenuItemData> menuItemDataList = new ArrayList<>();
    private final MenuDescriptor menuDescriptor;

    protected BaseMenu(StaticMenu staticMenu) {
        this.menuDescriptor = StaticMenu.toDescriptor(staticMenu);
    }

    protected BaseMenu(MenuDescriptor menuDescriptor) {
        this.menuDescriptor = menuDescriptor;
    }

    public static void printWrapped(String text, int lineWidth) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() > lineWidth) {
                System.out.println(line);
                line = new StringBuilder(word);
            } else {
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            }
        }

        if (!line.isEmpty()) {
            System.out.println(line);
        }
    }

    //region Menu implementation
    @Override
    public MenuDescriptor getDescriptor() {
        return menuDescriptor;
    }

    @Override
    public MenuItemDescriptor getItemDescriptor(String inputKey) throws NewsAggregatorNotFoundException {

        if (menuItemDescriptorByKey.containsKey(inputKey)) {
            return menuItemDescriptorByKey.get(inputKey);
        } else {
            throw new NewsAggregatorNotFoundException(Errors.UNKNOWN_MENU_KEY);
        }
    }
    //endregion Menu implementation

    //region Protected helpers

    @Override
    public void print() {
        printMenuHeader();
        printMenuDescription();
        printMenuItems();
        printMenuFooter();
    }

    /**
     * Добавляет элемент статического меню.
     *
     * @param inputKey       Идентификатор элемента меню (вводится с клавиатуры).
     * @param staticMenuItem Элемент перечисления, описывающего элемент меню.
     */
    protected void addMenuItem(String inputKey, StaticMenuItem staticMenuItem) {
        MenuItemDescriptor menuItemDescriptor = StaticMenuItem.toDescriptor(staticMenuItem);

        menuItemDescriptorByKey.put(inputKey, menuItemDescriptor);
        menuItemDataList.add(new MenuItemData(inputKey, menuItemDescriptor));
    }

    /**
     * Добавляет элемент динамического меню.
     *
     * @param inputKey           Идентификатор элемента меню (вводится с клавиатуры).
     * @param menuItemDescriptor Объект, описывающий элемент меню.
     */
    protected void addMenuItem(String inputKey, MenuItemDescriptor menuItemDescriptor) {
        menuItemDescriptorByKey.put(inputKey, menuItemDescriptor);
        menuItemDataList.add(new MenuItemData(inputKey, menuItemDescriptor));
    }

    /**
     * Выводит на экран заголовок меню.
     */
    protected void printMenuHeader() {
        System.out.printf("%n=== %s ===%n", menuDescriptor.getTitle());
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
        menuItemDataList.forEach(menuItemData -> System.out.printf(
                "[%s]. %s%n",
                menuItemData.inputKey(),
                menuItemData.menuItemDescriptor().getTitle()
        ));
    }

    /**
     * Выводит на экран нижнее обрамление меню.
     */
    protected void printMenuFooter() {
        System.out.println("==========================");
    }
    //endregion Protected helpers

    private record MenuItemData(String inputKey, MenuItemDescriptor menuItemDescriptor) {
    }

    public static class Builder extends MenuBuilder<BaseMenu, BaseMenu.Builder> {

        @Override
        protected BaseMenu createMenu() {
            return new BaseMenu(getMenuDescriptor());
        }

        @Override
        protected Builder getBuilder() {
            return this;
        }
    }
}
