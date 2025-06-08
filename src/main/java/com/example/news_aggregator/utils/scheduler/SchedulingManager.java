package com.example.news_aggregator.utils.scheduler;

import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.service.clean.CleanService;
import com.example.news_aggregator.service.parser.NewsParser;
import com.example.news_aggregator.service.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingManager {

    private final NewsParser newsParser;
    private final SettingService settingService;
    private final CleanService cleanService;

    @Autowired
    public SchedulingManager(NewsParser newsParser,
                             SettingService settingService,
                             CleanService cleanService) {
        this.newsParser = newsParser;
        this.settingService = settingService;
        this.cleanService = cleanService;
    }

    @Bean
    public String autoParsingFrequencySetting() {
        return settingService.getSettingValueById(SettingType.AUTO_PARSING_FREQUENCY.getId());
    }

    @Scheduled(cron = "#{autoParsingFrequencySetting}")
    private void parseNews() {
        newsParser.parseAll();
    }

    @Scheduled(cron = "#{autoParsingFrequencySetting}")
    private void clearNews() {
        cleanService.deleteOldNews();
    }
}
