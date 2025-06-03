package com.example.news_aggregator.service.parser;

import com.example.news_aggregator.model.news.Source;

public interface NewsParser {

    void parseAll();

    void parse(Source source);
}
