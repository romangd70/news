package com.example.news_aggregator;

import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.repository.NewsRepository;
import com.example.news_aggregator.service.filter.FilterServiceImpl;
import com.example.news_aggregator.service.filter.NewsFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FilterServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private FilterServiceImpl filterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilterByDate() {
        // Создаем данные для тестирования
        String date = "05-05-2025";
        NewsFilter filter = NewsFilter.builder().byDate(date).build();

        LocalDate localDate = LocalDate.of(2025, 5, 5);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);

        // Моки объектов новостей
        List<News> expectedNews = Arrays.asList(
                new News(),
                new News()
        );

        when(newsRepository.findAllByPublicationDateBetween(startOfDay, endOfDay)).thenReturn(expectedNews);

        // Тест
        List<News> result = filterService.filter(filter);

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAllByPublicationDateBetween(startOfDay, endOfDay);
    }

    @Test
    void testFilterBySource() {
        // Создаем данные для тестирования
        Integer sourceId = 1;
        NewsFilter filter = NewsFilter.builder().bySource(sourceId).build();
        filter.setBySource(sourceId);

        // Моки объектов новостей
        List<News> expectedNews = Arrays.asList(
                new News(),
                new News()
        );

        when(newsRepository.findAllBySourceId(sourceId)).thenReturn(expectedNews);

        // Тест
        List<News> result = filterService.filter(filter);

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAllBySourceId(sourceId);
    }

    @Test
    void testFilterByCategory() {
        // Создаем данные для тестирования
        Integer categoryId = 2;
        NewsFilter filter = NewsFilter.builder().byCategory(categoryId).build();
        filter.setByCategory(categoryId);

        // Моки объектов News
        List<News> expectedNews = Arrays.asList(
                new News(),
                new News()
        );

        when(newsRepository.findAllByCategoryId(categoryId)).thenReturn(expectedNews);

        // Тест
        List<News> result = filterService.filter(filter);

        //Проверяем результаты тестирования
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAllByCategoryId(categoryId);
    }

    @Test
    void testFilterByKeyword() {
        // Создаем данные для тестирования
        String keyword = "В мире";
        NewsFilter filter = NewsFilter.builder().byKeyword(keyword).build();
        filter.setByKeyword(keyword);

        // Моки объектов News
        List<News> expectedNews = Arrays.asList(new News(),
                new News()
        );

        when(newsRepository.findNewsBySimilarKeywords(keyword)).thenReturn(expectedNews);

        // Тест
        List<News> result = filterService.filter(filter);

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findNewsBySimilarKeywords(keyword);
    }

    @Test
    void testFilterEmpty() {
        // Создаем данные для тестирования
        NewsFilter filter = NewsFilter.builder().build();

        // Тест
        List<News> result = filterService.filter(filter);

        // Проверяем результаты тестирования
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(newsRepository);
    }
}
