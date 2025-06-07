package com.example.news_aggregator.enums;

import com.example.news_aggregator.common.EnumWithId;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LogActionType implements EnumWithId {

    ADD_NEWS_ACTION(1),
    DELETE_NEWS_ACTION(2);

    private final int id;

    LogActionType(int id) {
        this.id = id;
    }

    public static LogActionType getById(int id) {
        return Arrays.stream(LogActionType.values())
                .filter(logActionType -> logActionType.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NewsAggregatorNotFoundException(Errors.LOG_ACTION_TYPE_ID_S_NOT_FOUND, id));
    }
}
