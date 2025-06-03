package com.example.news_aggregator.menu.main.command;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.menu.impl.BaseMenuCommand;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.service.statistics.StatisticsService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
public class TopThemesStatisticsCommand extends BaseMenuCommand {

    private final StatisticsService statisticsService;

    public TopThemesStatisticsCommand(StatisticsService statisticsService) {
        super(StaticMenuItem.TOP_THEMES_STATISTICS);
        this.statisticsService = statisticsService;
    }

    @Override
    public void execute(Scanner scanner) throws NewsAggregatorIllegalArgumentException {
        Map<String, Long> result = statisticsService.getTopThemes();
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
