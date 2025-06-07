package com.example.news_aggregator.service.filter;

import com.example.news_aggregator.model.news.News;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public interface FilterService {

    List<News> filter(NewsFilter filter);

    @Getter
    @Setter
    @Builder
    class NewsFilter {
        private String byDate;
        private Integer bySource;
        private Integer byCategory;
        private String byKeyword;
    }
}
