package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

@Component
public class FilterNewsSwitcher extends BaseMenuSwitcher {

    protected FilterNewsSwitcher() {
        super(StaticMenuItem.FILTER_NEWS_SWITCHER, StaticMenu.FILTER);
    }
}
