package com.example.news_aggregator.service.filter;

import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {

    private final NewsRepository newsRepository;

    @Autowired
    public FilterServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> filter(NewsFilter filter) {
        if (filter.getByDate() != null && !filter.getByDate().isEmpty()) {
            LocalDate localDate;
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                localDate = LocalDate.parse(filter.getByDate(), dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("Неверный формат ввода!");
                return Collections.emptyList();
            }
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDay = localDate.atTime(23, 59, 59);
            return newsRepository.findAllByPublicationDateBetween(startOfDay, endOfDay);
        } else if (filter.getBySource() != null) {
            return newsRepository.findAllBySourceId(filter.getBySource());
        } else if (filter.getByCategory() != null) {
            return newsRepository.findAllByCategoryId(filter.getByCategory());
        } else if (filter.getByKeyword() != null) {
            return newsRepository.findNewsBySimilarKeywords(filter.getByKeyword());
        }
        return Collections.EMPTY_LIST;
    }
}
