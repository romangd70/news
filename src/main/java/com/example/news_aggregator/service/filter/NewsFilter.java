package com.example.news_aggregator.service.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class NewsFilter {

    private String byDate;
    private Integer bySource;
    private Integer byCategory;
    private String byKeyword;
}
