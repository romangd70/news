package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MenuBuilder<T extends BaseMenu, B extends MenuBuilder<?, ?>> {

    private final Map<String, MenuItemDescriptor> menuItemDescriptorByKey = new HashMap<>();
    // Коллекция для сохранения порядка добавления элементов
    private final List<String> menuItemInputKeys = new ArrayList<>();
    @Getter
    private MenuDescriptor menuDescriptor = null;

    protected abstract T createMenu();

    protected abstract B getBuilder();

    public B withMenuDescriptor(MenuDescriptor menuDescriptor) {
        this.menuDescriptor = menuDescriptor;
        return getBuilder();
    }

    public B withMenuItem(String key, MenuItemDescriptor menuItemDescriptor) {
        menuItemDescriptorByKey.put(key, menuItemDescriptor);
        menuItemInputKeys.add(key);
        return getBuilder();
    }

    public Menu build() {
        T result = createMenu();
        menuItemInputKeys.forEach(inputKey -> {
            MenuItemDescriptor menuItemDescriptor = menuItemDescriptorByKey.get(inputKey);
            result.addMenuItem(inputKey, menuItemDescriptor);
        });
        return result;
    }
}