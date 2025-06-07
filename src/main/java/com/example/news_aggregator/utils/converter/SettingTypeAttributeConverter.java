package com.example.news_aggregator.utils.converter;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.enums.SettingType;

public class SettingTypeAttributeConverter extends BaseAttributeConverter<SettingType> {

    @Override
    public SettingType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return SettingType.getById(dbData);
        } catch (NewsAggregatorNotFoundException e) {
            throw new NewsAggregatorIllegalArgumentException(
                    Errors.UNSUPPORTED_SETTING_TYPE_ID_S, SettingType.class.getName(), dbData
            );
        }
    }
}
