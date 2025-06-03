package com.example.news_aggregator.common.exception;

import com.example.news_aggregator.enums.Errors;
import lombok.Getter;

@Getter
public abstract class NewsAggregatorRuntimeException extends RuntimeException {

    private final Errors type;
    private final transient Object[] parameters;

    protected NewsAggregatorRuntimeException(Errors type, Object... parameters) {
        super(
                parameters == null || parameters.length == 0
                        ? type.getDefaultMessage()
                        : String.format(type.getDefaultMessage(), parameters)
        );
        this.type = type;
        this.parameters = parameters;
    }
}
