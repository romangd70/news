package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuItemDescriptorImpl implements MenuItemDescriptor {

    private final String id;
    private final String title;
    private final boolean isDynamic;
}
