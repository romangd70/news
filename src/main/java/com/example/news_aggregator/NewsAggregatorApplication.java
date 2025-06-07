package com.example.news_aggregator;

import com.example.news_aggregator.common.menu.MenuRegistrar;
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

/**
 * Главный класс приложения Spring.
 * Наследование от ApplicationRunner позволяет обернуть параметры командной строки в объект ApplicationArguments.
 * При необходимости работать с аргументами как массивом можно переключиться на использование CommandLineRunner.
 * Мы не обрабатываем аргументы, поэтому выбор не важен.
 */
@SpringBootApplication
public class NewsAggregatorApplication implements ApplicationRunner {

    private final MenuRunner menuRunner;
    private final MenuRegistrar menuRegistrar;
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    @Autowired
    public NewsAggregatorApplication(
            MenuRunner menuRunner,
            MenuRegistrar menuRegistrar,
            JdbcTemplate jdbcTemplate,
            ResourceLoader resourceLoader
    ) {
        this.menuRunner = menuRunner;
        this.menuRegistrar = menuRegistrar;
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Статический метод входа для JVM.
     * Именно тут мы создадим и раскрутим маховик инъекции зависимостей Spring.
     *
     * @param args Параметры командной строки.
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(NewsAggregatorApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    //region ApplicationRunner implementation

    /**
     * Точка входа в бизнес-логику приложения.
     *
     * @param args Параметры командной строки.
     * @throws Exception Все исключения, не обработанные внутри главного цикла.
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Загрузка SQL-скрипта из ресурсов
//        Resource resource = resourceLoader.getResource("classpath:scripts/script.sql");
//        String sql = new BufferedReader(new InputStreamReader(resource.getInputStream()))
//                .lines()
//                .collect(Collectors.joining("\n"));
//
//        // Выполнение SQL-скрипта
//        jdbcTemplate.execute(sql);
//        System.out.println();
//        System.out.println("<*> SQL-скрипт успешно выполнен!");

        // Регистрация статических меню
        menuRegistrar.registerStaticMenus();
        System.out.println("<*> Регистрация статических меню успешно выполнена!");

        // Запуск главного цикла приложения
        // Статическое меню 'MAIN' будет установлено в качестве стартового контекста ввода
        System.out.println("<*> Консольное приложение запущено!");
        menuRunner.run(StaticMenu.MAIN);
    }
    //endregion ApplicationRunner implementation
}
