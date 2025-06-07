package com.example.news_aggregator.service.setting;

import com.example.news_aggregator.model.settings.Setting;

public interface SettingService {

    String getSettingValueById(int id);

    Setting updateSetting(int id, String value);
}
