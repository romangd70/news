package com.example.news_aggregator.menu.main.command;

import com.example.news_aggregator.common.menu.impl.BaseMenuCommand;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.service.parser.NewsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ParseNewsCommand extends BaseMenuCommand {

    private final NewsParser newsParser;

    @Autowired
    public ParseNewsCommand(NewsParser newsParser) {
        super(StaticMenuItem.PARSE_NEWS_COMMAND);
        this.newsParser = newsParser;
    }

    @Override
    public void execute(Scanner scanner) {
        newsParser.parseAll();
    }
}