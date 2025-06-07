package com.example.news_aggregator.common.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Регистратор статических меню.
 * Его единственное предназначение собрать компоненты статических меню и произвести их регистрацию в реестре.
 * Операцию сбора компонентов меню производить непосредственно в реестре нельзя, так как это приводит к появлению
 * перекрестных ссылок между реестром и элементами меню (для создания реестра нужны элементы меню, которым нужен реестр).
 * Регистрация должна быть произведена до того, как главный цикл приложения начнет работу.
 */
@Component
public class MenuRegistrar {

    private final MenuRegistry menuRegistry;
    private final List<Menu> menus;
    private final List<MenuItem> menuItems;

    @Autowired
    public MenuRegistrar(
            MenuRegistry menuRegistry,
            List<Menu> menus,
            List<MenuItem> menuItems
    ) {
        this.menuRegistry = menuRegistry;
        this.menus = menus;
        this.menuItems = menuItems;
    }

    /**
     * Регистрирует меню в реестре.
     * Этот метод должен быть вызван единожды перед стартом главного цикла приложения.
     */
    public void registerStaticMenus() {
        menus.forEach(menuRegistry::registerMenu);
        menuItems.forEach(menuRegistry::registerMenuItem);
    }
}
