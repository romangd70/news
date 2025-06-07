package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuSwitcher;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

/**
 * Базовая реализация переключателя меню.
 */
@Getter
public class BaseMenuSwitcher
        extends BaseMenuCommand
        implements MenuSwitcher {

    @Setter(AccessLevel.PROTECTED)
    private String nextMenuId = null;

    /**
     * Конструктор для создания статического переключателя на статическое меню.
     *
     * @param staticMenuItem Статическое описание переключателя.
     * @param nextStaticMenu Статическое описание целевого меню.
     */
    protected BaseMenuSwitcher(
            StaticMenuItem staticMenuItem,
            StaticMenu nextStaticMenu
    ) {
        super(staticMenuItem);

        this.nextMenuId = nextStaticMenu.name();
    }

    /**
     * Конструктор для создания статического переключателя на динамическое меню.
     * Внимание: nextMenuId должен быть установлен в методе execute.
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
     * @param menuItemId Идентификатор переключателя.
     * @param title      Наименование элемента меню.
     * @param nextMenuId Идентификатор целевого меню.
     */
    public BaseMenuSwitcher(
            String menuItemId,
            String title,
            String nextMenuId
    ) {
        super(menuItemId, title, true);

        this.nextMenuId = nextMenuId;
    }

    @Override
    public void execute(Scanner scanner) {
        // Базовая реализация не требуется
        // Переключатели на динамические меню должны установить идентификатор следующего меню перед выходом из метода.
    }
}