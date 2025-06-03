package com.example.news_aggregator.utils.converter;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.enums.LogActionType;

public class LogActionTypeAttributeConverter extends BaseAttributeConverter<LogActionType> {

    @Override
    public LogActionType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return LogActionType.getById(dbData);
        } catch (NewsAggregatorNotFoundException e) {
            throw new NewsAggregatorIllegalArgumentException(
                    Errors.UNSUPPORTED_LOG_ACTION_TYPE_ID_S, LogActionType.class.getName(), dbData
            );
        }
    }
}
