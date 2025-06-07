package com.example.news_aggregator.service.clean;

import com.example.news_aggregator.enums.LogActionType;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.repository.NewsRepository;
import com.example.news_aggregator.service.log.LogActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CleanServiceImpl implements CleanService {

    private final NewsRepository newsRepository;
    private final LogActionService logActionService;

    @Autowired
    public CleanServiceImpl(NewsRepository newsRepository,
                            LogActionService logActionService) {
        this.newsRepository = newsRepository;
        this.logActionService = logActionService;
    }

    @Override
    @Transactional
    public void deleteOldNews() {
        List<News> oldNews = newsRepository.getNewsOlderThan(LocalDateTime.now().minusMonths(1));

        Set<Long> idsToDelete = new HashSet<>();
        for (News news : oldNews) {
            idsToDelete.add(news.getId());
        }
        // Логируем процесс удаления
        logActionService.log(LogActionType.DELETE_NEWS_ACTION, idsToDelete);
        newsRepository.deleteAllByIdIn(idsToDelete);
    }
}
