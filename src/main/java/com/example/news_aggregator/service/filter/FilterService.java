package com.example.news_aggregator.service.filter;

import com.example.news_aggregator.model.news.News;

import java.util.List;

public interface FilterService {

    List<News> filter(NewsFilter filter);
}
