package com.example.news_aggregator.menu.main;

import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticsMenu extends BaseMenu {

    @Autowired
    protected StatisticsMenu(MenuRegistry menuRegistry) {
        super(StaticMenu.STATISTICS, menuRegistry);

        // Выход в главное меню
        addMenuItem("1", StaticMenuItem.BY_CATEGORY_STATISTICS);
        addMenuItem("2", StaticMenuItem.TOP_THEMES_STATISTICS);
        addMenuItem("3", StaticMenuItem.DYNAMIC_POPULARITY_STATISTICS);
        addMenuItem("0", StaticMenuItem.MAIN_MENU_SWITCHER);

    }
}
