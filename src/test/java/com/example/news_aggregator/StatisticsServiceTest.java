package com.example.news_aggregator;

import com.example.news_aggregator.repository.NewsRepository;
import com.example.news_aggregator.service.statistics.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNewsCountByCategory() {
        // Создаем данные для тестирования
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"В мире", 10L},
                new Object[]{"Политика", 5L}
        );
        when(newsRepository.countNewsByCategory()).thenReturn(mockResults);

        // Тест
        Map<String, Long> result = statisticsService.getNewsCountByCategory();

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10L, result.get("В мире"));
        assertEquals(5L, result.get("Политика"));
        verify(newsRepository, times(1)).countNewsByCategory();
    }

    @Test
    void testGetTopThemes() {
        // Создаем данные для тестирования
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"ИИ", 15L},
                new Object[]{"Изменения климата", 8L},
                new Object[]{"Блокчейн", 12L}
        );
        when(newsRepository.findTopKeywords()).thenReturn(mockResults);

        // Тест
        Map<String, Long> result = statisticsService.getTopThemes();

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertEquals(3, result.size());
        List<String> expectedOrder = Arrays.asList("ИИ", "Блокчейн", "Изменения климата");
        assertEquals(expectedOrder, new ArrayList<>(result.keySet()));
        assertEquals(15L, result.get("ИИ"));
        assertEquals(12L, result.get("Блокчейн"));
        assertEquals(8L, result.get("Изменения климата"));
        verify(newsRepository, times(1)).findTopKeywords();
    }

    @Test
    void testGetNewsCountByCategory_NoData() {
        // Создаем данные для тестирования
        when(newsRepository.countNewsByCategory()).thenReturn(Collections.emptyList());

        // Тест
        Map<String, Long> result = statisticsService.getNewsCountByCategory();

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(newsRepository, times(1)).countNewsByCategory();
    }

    @Test
    void testGetTopThemes_NoData() {
        // Создаем данные для тестирования
        when(newsRepository.findTopKeywords()).thenReturn(Collections.emptyList());

        // Тест
        Map<String, Long> result = statisticsService.getTopThemes();

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(newsRepository, times(1)).findTopKeywords();
    }

    @Test
    void testFindKeywordTrends_NoData() {
        // Создаем данные для тестирования
        LocalDateTime startDate = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 10, 31, 23, 59);

        when(newsRepository.findKeywordTrends(startDate, endDate)).thenReturn(Collections.emptyList());

        // Тест
        Map<String, Map<LocalDate, Long>> result = statisticsService.findKeywordTrends(startDate, endDate);

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(newsRepository, times(1)).findKeywordTrends(startDate, endDate);
    }
}
