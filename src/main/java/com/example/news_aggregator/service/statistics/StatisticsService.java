package com.example.news_aggregator.service.statistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface StatisticsService {

    Map<String, Long> getNewsCountByCategory();

    Map<String, Long> getTopThemes();

    Map<String, Map<LocalDate, Long>> findKeywordTrends(LocalDateTime startDate, LocalDateTime endDate);
}
