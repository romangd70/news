package com.example.news_aggregator.service.statistics;

import com.example.news_aggregator.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final NewsRepository newsRepository;

    @Autowired
    public StatisticsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public Map<String, Long> getNewsCountByCategory() {
        List<Object[]> results = newsRepository.countNewsByCategory();
        Map<String, Long> categoryCounts = new TreeMap<>();

        for (Object[] result : results) {
            String categoryName = (String) result[0]; // название категории
            Long count = (Long) result[1]; // счетчик новостей в категории
            categoryCounts.put(categoryName, count);
        }

        return categoryCounts;
    }

    @Override
    public Map<String, Long> getTopThemes() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> topKeywords = newsRepository.findTopKeywords();

        for (Object[] row : topKeywords) {
            String keyword = (String) row[0]; // Ключевое слово
            Long frequency = ((Number) row[1]).longValue(); // Частота упоминания
            result.put(keyword, frequency);
        }

        // Сортируем Map по значениям
        List<Map.Entry<String, Long>> entries = new ArrayList<>(result.entrySet());
        entries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue())); // Сортировка по убыванию

        Map<String, Long> sortedResult = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entries) {
            sortedResult.put(entry.getKey(), entry.getValue());
        }

        return sortedResult;
    }

    /**
     * Метод отображает динамику появления новостей по ключевым словам.
     *
     * @param startDate - начальное время поиска
     * @param endDate   - конечное время поиска
     * @return ответ в виде мапы, где ключ - ключевое слово,
     * а значение - мапа с ключом в виде даты и значением в виде количества упоминаний в этот день
     */
    @Override
    public Map<String, Map<LocalDate, Long>> findKeywordTrends(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> rawTrends = newsRepository.findKeywordTrends(startDate, endDate);

        // Преобразуем и сортируем данные
        Map<String, Map<LocalDate, Long>> trends = new TreeMap<>();

        for (Object[] row : rawTrends) {
            String keyword = (String) row[0];
            LocalDate period = ((LocalDate) row[1]);
            Long count = ((Number) row[2]).longValue();

            trends.computeIfAbsent(keyword, k -> new TreeMap<>()).put(period, count);
        }

        return trends;
    }
}
