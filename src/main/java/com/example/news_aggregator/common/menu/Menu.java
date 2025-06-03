package com.example.news_aggregator.common.menu;

import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;

public interface Menu {

    MenuDescriptor getDescriptor();

    MenuItemDescriptor getItemDescriptor(String inputKey) throws NewsAggregatorNotFoundException;

    void print();
}
