package com.example.news_aggregator.menu.main;

import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReadMenu extends BaseMenu {

    @Autowired
    protected ReadMenu(MenuRegistry menuRegistry) {
        super(StaticMenu.READ_NEWS, menuRegistry);

        addMenuItem("1", StaticMenuItem.ALL_NEWS_SWITCHER);
        addMenuItem("2", StaticMenuItem.FILTER_NEWS_SWITCHER);

        // Выход в главное меню
        addMenuItem("0", StaticMenuItem.MAIN_MENU_SWITCHER);
    }
}
