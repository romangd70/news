package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.settings.Setting;
import com.example.news_aggregator.service.setting.SettingService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SettingSortingSwitcher extends BaseMenuSwitcher {

    private final SettingService settingService;

    protected SettingSortingSwitcher(SettingService settingService) {
        super(StaticMenuItem.SORTING_SETTING, StaticMenu.SETTINGS);
        this.settingService = settingService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите индекс типа сортировки 1 - По дате, 2 - По заголовку, 3 - По источнику");

        String value = scanner.nextLine();
        Setting setting = settingService.updateSetting(SettingType.SORTING_TYPE.getId(), value);
        if (setting != null) {
            System.out.println("Настройка успешно обновлена.");
        }
    }
}
