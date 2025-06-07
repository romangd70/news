package com.example.news_aggregator.service.log;

import com.example.news_aggregator.enums.LogActionType;
import com.example.news_aggregator.model.logs.LogAction;
import com.example.news_aggregator.repository.LogActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class LogActionServiceImpl implements LogActionService {

    private final LogActionRepository logActionRepository;

    @Autowired
    public LogActionServiceImpl(LogActionRepository logActionRepository) {
        this.logActionRepository = logActionRepository;
    }

    @Override
    public void log(LogActionType type, Set<Long> targetObjectIds) {
        LocalDateTime now = LocalDateTime.now();
        Set<LogAction> logActionsToSave = new HashSet<>();
        for (Long targetObjectId : targetObjectIds) {
            logActionsToSave.add(new LogAction(null, type, targetObjectId, now));
        }
        logActionRepository.saveAll(logActionsToSave);
    }
}
