package com.example.news_aggregator.menu.main;

import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenu extends BaseMenu {

    @Autowired
    protected MainMenu(MenuRegistry menuRegistry) {
        super(StaticMenu.MAIN, menuRegistry);

        addMenuItem("1", StaticMenuItem.PARSE_NEWS_COMMAND);
        addMenuItem("2", StaticMenuItem.READ_NEWS_SWITCHER);
        addMenuItem("3", StaticMenuItem.DISPLAY_STATISTICS_SWITCHER);
        addMenuItem("4", StaticMenuItem.SETTINGS);

        // Выход из приложения
        addMenuItem("0", StaticMenuItem.APPLICATION_EXIT_COMMAND);
    }
}
