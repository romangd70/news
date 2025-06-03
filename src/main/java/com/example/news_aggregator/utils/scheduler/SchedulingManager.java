package com.example.news_aggregator.utils.scheduler;

import com.example.news_aggregator.service.parser.NewsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingManager {

    private final NewsParser newsParser;

    @Autowired
    public SchedulingManager(NewsParser newsParser) {
        this.newsParser = newsParser;
    }

    // TODO: из настроек
    @Scheduled(cron = "0 0 8 * * *")
    private void parseNews() {
        newsParser.parseAll();
    }
}
