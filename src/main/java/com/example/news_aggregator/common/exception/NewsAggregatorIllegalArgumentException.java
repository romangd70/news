package com.example.news_aggregator.common.exception;

import com.example.news_aggregator.enums.Errors;

public class NewsAggregatorIllegalArgumentException extends NewsAggregatorRuntimeException {

    public NewsAggregatorIllegalArgumentException(Errors type, Object... parameters) {
        super(type, parameters);
    }
}
