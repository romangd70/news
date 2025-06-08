package com.example.news_aggregator.menu.main;

import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

@Component
public class FilterMenu extends BaseMenu {

    protected FilterMenu() {
        super(StaticMenu.FILTER);

        addMenuItem("1", StaticMenuItem.BY_DATE_SWITCHER);
        addMenuItem("2", StaticMenuItem.BY_SOURCE_SWITCHER);
        addMenuItem("3", StaticMenuItem.BY_CATEGORY_SWITCHER);
        addMenuItem("4", StaticMenuItem.BY_KEYWORDS_SWITCHER);

        // Выход в главное меню
        addMenuItem("0", StaticMenuItem.MAIN_MENU_SWITCHER);
    }
}
