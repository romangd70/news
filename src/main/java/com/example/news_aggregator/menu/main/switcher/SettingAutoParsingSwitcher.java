package com.example.news_aggregator.menu.main.switcher;

import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.model.settings.Setting;
import com.example.news_aggregator.service.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SettingAutoParsingSwitcher extends BaseMenuSwitcher {

    private final SettingService settingService;

    @Autowired
    protected SettingAutoParsingSwitcher(SettingService settingService) {
        super(StaticMenuItem.AUTO_PARSING_TIME, StaticMenu.SETTINGS);
        this.settingService = settingService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите cron выражение, состоящее из 6 символов (например, 0 0 8 * * *), чтобы установить частоту автоматического парсинга новостей");

        String cron = scanner.nextLine();
        Setting setting = settingService.updateSetting(SettingType.AUTO_PARSING_FREQUENCY.getId(), cron);
        if (setting != null) {
            System.out.println("Настройка успешно обновлена.");
        }
    }
}
