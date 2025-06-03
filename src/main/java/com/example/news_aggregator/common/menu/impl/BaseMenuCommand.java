package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuCommand;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.menu.StaticMenuItem;

public abstract class BaseMenuCommand implements MenuCommand {

    private final MenuItemDescriptor menuItemDescriptor;

    protected BaseMenuCommand(
            StaticMenuItem staticMenuItem
    ) {
        this.menuItemDescriptor = StaticMenuItem.toDescriptor(staticMenuItem);
    }

    protected BaseMenuCommand(
            MenuItemDescriptor menuItemDescriptor
    ) {
        this.menuItemDescriptor = menuItemDescriptor;
    }

    @Override
    public MenuItemDescriptor getDescriptor() {
        return menuItemDescriptor;
    }
}
