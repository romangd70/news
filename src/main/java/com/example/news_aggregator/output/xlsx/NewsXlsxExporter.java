package com.example.news_aggregator.output.xlsx;

import com.example.news_aggregator.model.news.News;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Компонент экспорта новостей в xlsx файл.
 */
@Component
public class NewsXlsxExporter {

    private static final String REPORT_NAME_S_DATE_TIME_S = "%s_%s_";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String XLSX_EXTENSION = ".xlsx";

    private static final int DATE_COLUMN_IDX = 0;
    private static final int CATEGORY_COLUMN_IDX = 1;
    private static final int SOURCE_COLUMN_IDX = 2;
    private static final int TITLE_COLUMN_IDX = 3;
    private static final int DESCRIPTION_COLUMN_IDX = 4;
    private static final int CONTENT_COLUMN_IDX = 5;

    private static final int COLUMN_COUNT = 6;

    /**
     * Упрощенный экспорт данных в xlsx формате.
     *
     * @param reportName   Префикс, который будет добавлен к имени итогового файла.
     * @param newsToExport Список новостей для выгрузки.
     * @return Имя файла, содержащего выгруженные данные.
     * @throws IOException В случае ошибок вывода в файл.
     */
    public String export(
            String reportName,
            List<News> newsToExport
    ) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            int sheetIndex = workbook.getSheetIndex(sheet);
            workbook.setSheetName(sheetIndex, "Новости");

            // Вывод заголовка
            int rowIdx = 0;
            Row headerRow = sheet.createRow(rowIdx);
            for (int i = 0; i < COLUMN_COUNT; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(getColumnName(i));
            }

            // Вывод данных
            for (News newsItem : newsToExport) {
                Row row = sheet.createRow(++rowIdx);
                for (int i = 0; i < COLUMN_COUNT; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(getValue(i, newsItem));
                }
            }

            // Файл во временной папке сисстемы
            String nowDateTimeStr = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            File reportFile = File.createTempFile(
                    String.format(REPORT_NAME_S_DATE_TIME_S, reportName, nowDateTimeStr),
                    XLSX_EXTENSION
            );

            // Запись в файл
            try (OutputStream outputStream = new FileOutputStream(reportFile)) {
                workbook.write(outputStream);
            }

            return reportFile.getAbsolutePath();
        }
    }

    private String getColumnName(int columnIdx) {
        return switch (columnIdx) {
            case DATE_COLUMN_IDX -> "Дата публикации";
            case CATEGORY_COLUMN_IDX -> "Категория";
            case SOURCE_COLUMN_IDX -> "Источник";
            case TITLE_COLUMN_IDX -> "Заголовок";
            case DESCRIPTION_COLUMN_IDX -> "Описание";
            case CONTENT_COLUMN_IDX -> "Содержимое";
            default -> "";
        };
    }

    private String getValue(int columnIdx, News newsItem) {
        return switch (columnIdx) {
            case DATE_COLUMN_IDX -> newsItem.getPublicationDate().toString();
            case CATEGORY_COLUMN_IDX -> newsItem.getCategory().getName();
            case SOURCE_COLUMN_IDX -> newsItem.getSource().getName();
            case TITLE_COLUMN_IDX -> newsItem.getTitle();
            case DESCRIPTION_COLUMN_IDX -> newsItem.getDescription();
            case CONTENT_COLUMN_IDX -> newsItem.getContent();
            default -> "";
        };
    }
}
