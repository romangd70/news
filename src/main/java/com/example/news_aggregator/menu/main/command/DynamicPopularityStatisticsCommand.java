package com.example.news_aggregator.menu.main.command;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.menu.impl.BaseMenuCommand;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.menu.StaticMenuItem;
import com.example.news_aggregator.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

@Component
public class DynamicPopularityStatisticsCommand extends BaseMenuCommand {

    public static final String MESSAGE = "Введите даты через пробел в формате dd-MM-yyyy dd-MM-yyyy (например: 01-01-2023 31-12-2023)";

    private final StatisticsService statisticsService;

    @Autowired
    public DynamicPopularityStatisticsCommand(StatisticsService statisticsService) {
        super(StaticMenuItem.DYNAMIC_POPULARITY_STATISTICS);
        this.statisticsService = statisticsService;
    }


    @Override
    public void execute(Scanner scanner) throws NewsAggregatorIllegalArgumentException {
        String[] dates = null;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        // Определяем формат даты
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Цикл для ввода дат
        while (true) {
            System.out.println(MESSAGE);
            String input = scanner.nextLine().trim();

            try {
                // Разделяем ввод на две даты
                dates = input.split(" ");
                if (dates.length != 2) {
                    throw new NewsAggregatorIllegalArgumentException(Errors.TWO_DATES_NOT_SET);
                }

                // Парсим даты
                LocalDate startDate = LocalDate.parse(dates[0], dateTimeFormatter);
                LocalDate endDate = LocalDate.parse(dates[1], dateTimeFormatter);

                startDateTime = startDate.atStartOfDay();
                endDateTime = endDate.atTime(23, 59, 59);

                // Проверяем, что первое время меньше или равно второму
                if (startDate.isAfter(endDate)) {
                    throw new NewsAggregatorIllegalArgumentException(Errors.ILLEGAL_DATE_ORDER);
                }

                // Если всё корректно, выходим из цикла
                break;
            } catch (DateTimeParseException e) {
                System.out.println(Errors.ILLEGAL_DATE_FORMAT.getDefaultMessage());
            } catch (Exception e) {
                System.out.println(Errors.UNHANDLED_ERROR_S.getDefaultMessage() + e.getMessage());
            }
        }

        Map<String, Map<LocalDate, Long>> result = statisticsService.findKeywordTrends(startDateTime, endDateTime);
        printTrends(result);
    }

    private void printTrends(Map<String, Map<LocalDate, Long>> trends) {
        // Формат даты для заголовков
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-MM-yyyy");

        // Собираем все уникальные даты из всех ключевых слов
        Set<LocalDate> allDates = new TreeSet<>(); // TreeSet для сортировки по возрастанию
        for (Map<LocalDate, Long> dateCounts : trends.values()) {
            allDates.addAll(dateCounts.keySet());
        }

        // Преобразуем Set в List для работы с индексами
        List<LocalDate> sortedDates = new ArrayList<>(allDates);

        // Заголовок таблицы
        System.out.print(String.format("%-80s", "Ключевое слово")); // Первый столбец — "Ключевое слово"
        for (LocalDate date : sortedDates) {
            System.out.print(String.format(" | %-10s", date.format(dateFormatter))); // Даты в формате "d-MM-yyyy"
        }
        System.out.println();
        System.out.println("=".repeat(20 + (sortedDates.size() * 14))); // Разделитель

        // Итерируемся по ключевым словам и выводим строку таблицы
        for (Map.Entry<String, Map<LocalDate, Long>> keywordEntry : trends.entrySet()) {
            String keyword = keywordEntry.getKey();
            Map<LocalDate, Long> dateCounts = keywordEntry.getValue();

            // Выводим первую ячейку строки — ключевое слово
            System.out.print(String.format("%-80s", keyword));

            // Заполняем значениями для каждой даты
            for (LocalDate date : sortedDates) {
                Long count = dateCounts.getOrDefault(date, 0L); // Если данных нет, выводим 0
                System.out.print(String.format(" | %-10d", count));
            }
            System.out.println(); // Переход на новую строку
        }
    }
}