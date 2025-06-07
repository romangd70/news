package com.example.news_aggregator.service.log;

import com.example.news_aggregator.enums.LogActionType;

import java.util.Set;

public interface LogActionService {
    
    void log(LogActionType type, Set<Long> targetObjectId);
}
