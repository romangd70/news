package com.example.news_aggregator.output.screen;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;

public class DynamicNewsMenuSwitcher extends BaseMenuSwitcher {

    public DynamicNewsMenuSwitcher(
            MenuItemDescriptor menuItemDescriptor,
            MenuDescriptor nextMenuDescriptor
    ) {
        super(menuItemDescriptor, nextMenuDescriptor);
    }
}
