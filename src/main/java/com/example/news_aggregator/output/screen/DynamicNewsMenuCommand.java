package com.example.news_aggregator.output.screen;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenuCommand;

import java.util.Scanner;

public class DynamicNewsMenuCommand extends BaseMenuCommand {

    public DynamicNewsMenuCommand(
            MenuItemDescriptor menuItemDescriptor
    ) {
        super(menuItemDescriptor);
    }

    @Override
    public void execute(Scanner scanner) throws NewsAggregatorIllegalArgumentException {

    }
}
