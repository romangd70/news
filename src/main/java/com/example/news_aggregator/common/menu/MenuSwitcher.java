package com.example.news_aggregator.common.menu;

public interface MenuSwitcher extends MenuCommand  {

    MenuDescriptor getNextMenuDescriptor();
}