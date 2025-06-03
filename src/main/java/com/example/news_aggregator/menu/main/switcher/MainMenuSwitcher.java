package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

@Component
public class MainMenuSwitcher extends BaseMenuSwitcher {

    public MainMenuSwitcher() {
        super(StaticMenuItem.MAIN_MENU_SWITCHER, StaticMenu.MAIN);
    }
}