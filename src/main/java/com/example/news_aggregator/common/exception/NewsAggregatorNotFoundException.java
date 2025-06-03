package com.example.news_aggregator.common.exception;

import com.example.news_aggregator.enums.Errors;

public class NewsAggregatorNotFoundException extends NewsAggregatorRuntimeException {

    public NewsAggregatorNotFoundException(Errors type, Object... parameters) {
        super(type, parameters);
    }
}
