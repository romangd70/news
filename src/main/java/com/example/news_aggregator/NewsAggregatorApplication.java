package com.example.news_aggregator;

import com.example.news_aggregator.common.menu.MenuRunner;
import com.example.news_aggregator.menu.StaticMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@SpringBootApplication
public class NewsAggregatorApplication implements ApplicationRunner {

    private final MenuRunner menuRunner;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    public NewsAggregatorApplication(
            MenuRunner menuRunner
    ) {
        this.menuRunner = menuRunner;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(NewsAggregatorApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Загрузка SQL-скрипта из ресурсов
        Resource resource = resourceLoader.getResource("classpath:scripts/script.sql");
        String sql = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        // Выполнение SQL-скрипта
        //jdbcTemplate.execute(sql);
        System.out.println("SQL-скрипт успешно выполнен!");

        System.out.println("=== Консольное приложение запущено ===");
        menuRunner.run(StaticMenu.MAIN);
    }
}
