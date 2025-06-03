package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuDescriptorImpl implements MenuDescriptor {

    private final String id;
    private final String title;
    private final boolean isDynamic;
}
