package com.example.news_aggregator.enums;

import com.example.news_aggregator.common.EnumWithId;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SettingType implements EnumWithId {

    AUTO_PARSING_FREQUENCY(1, "0 0 8 * * *"),
    SORTING_TYPE(2, "1");

    private final int id;
    private final String defaultValue;

    SettingType(int id, String defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public static SettingType getById(int id) {
        return Arrays.stream(SettingType.values())
                .filter(settingType -> settingType.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NewsAggregatorNotFoundException(Errors.LOG_ACTION_TYPE_ID_S_NOT_FOUND, id));
    }
}
