package com.example.news_aggregator.menu.main;

import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

@Component
public class SettingMenu extends BaseMenu {

    protected SettingMenu() {
        super(StaticMenu.SETTINGS);

        // Выход в главное меню
        addMenuItem("1", StaticMenuItem.AUTO_PARSING_TIME);
        addMenuItem("2", StaticMenuItem.SORTING_SETTING);

        addMenuItem("0", StaticMenuItem.MAIN_MENU_SWITCHER);

    }
}
