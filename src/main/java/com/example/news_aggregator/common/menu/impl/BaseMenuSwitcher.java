package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.common.menu.MenuSwitcher;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

@Getter
public class BaseMenuSwitcher
        extends BaseMenuCommand
        implements MenuSwitcher {

    @Setter(AccessLevel.PROTECTED)
    private MenuDescriptor nextMenuDescriptor = null;

    /**
     * Конструктор для создания стандартного переключателя.
     *
     * @param staticMenuItem Статическое описание переключателя.
     * @param nextStaticMenu Статическое описание целевого меню.
     */
    protected BaseMenuSwitcher(
            StaticMenuItem staticMenuItem,
            StaticMenu nextStaticMenu
    ) {
        super(staticMenuItem);

        this.nextMenuDescriptor = StaticMenu.toDescriptor(nextStaticMenu);
    }

    /**
     * Конструктор для создания переключателя на динамическое меню.
     *
     * @param staticMenuItem Статическое описание переключателя.
     */
    protected BaseMenuSwitcher(
            StaticMenuItem staticMenuItem
    ) {
        super(staticMenuItem);
    }

    /**
     * Конструктор для создания динамического переключателя на динамическое меню.
     *
     * @param menuItemDescriptor Дескриптор переключателя.
     * @param nextMenuDescriptor Дескриптор целевого меню.
     */
    protected BaseMenuSwitcher(
            MenuItemDescriptor menuItemDescriptor,
            MenuDescriptor nextMenuDescriptor
    ) {
        super(menuItemDescriptor);

        this.nextMenuDescriptor = nextMenuDescriptor;
    }

    @Override
    public void execute(Scanner scanner) {
        // Базовая реализация не требуется
    }
}
