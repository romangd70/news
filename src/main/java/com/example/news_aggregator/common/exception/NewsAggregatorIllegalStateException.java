package com.example.news_aggregator.common.exception;

import com.example.news_aggregator.enums.Errors;

public class NewsAggregatorIllegalStateException extends NewsAggregatorRuntimeException {

    public NewsAggregatorIllegalStateException(Errors type, Object... parameters) {
        super(type, parameters);
    }
}
