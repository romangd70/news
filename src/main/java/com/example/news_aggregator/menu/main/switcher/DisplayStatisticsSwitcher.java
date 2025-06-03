package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import org.springframework.stereotype.Component;

@Component
public class DisplayStatisticsSwitcher extends BaseMenuSwitcher {

    public DisplayStatisticsSwitcher() {
        super(
                StaticMenuItem.DISPLAY_STATISTICS_SWITCHER,
                StaticMenu.STATISTICS
        );
    }
}