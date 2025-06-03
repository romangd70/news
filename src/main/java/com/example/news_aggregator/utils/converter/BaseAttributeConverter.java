package com.example.news_aggregator.utils.converter;

import com.example.news_aggregator.common.EnumWithId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public abstract class BaseAttributeConverter<T extends EnumWithId>
        implements AttributeConverter<T, Integer> {

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        if (attribute == null)
            return null;

        return attribute.getId();
    }
}
