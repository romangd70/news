package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

@Component
public class ReadNewsSwitcher extends BaseMenuSwitcher {

    public ReadNewsSwitcher() {
        super(StaticMenuItem.READ_NEWS_SWITCHER, StaticMenu.READ_NEWS);
    }
}