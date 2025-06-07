package com.example.news_aggregator.output.xlsx;

import com.example.news_aggregator.common.menu.impl.BaseMenuCommand;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.model.news.News;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ExportNewsToXlsxCommand extends BaseMenuCommand {

    private final List<News> newsToExport;
    private final NewsXlsxExporter newsXlsxExporter;

    public ExportNewsToXlsxCommand(
            String id,
            String title,
            List<News> newsToExport,
            NewsXlsxExporter newsXlsxExporter
    ) {
        super(id, title, true);

        this.newsToExport = newsToExport;
        this.newsXlsxExporter = newsXlsxExporter;
    }

    @Override
    public void execute(Scanner scanner) {
        try {
            String fileName = newsXlsxExporter.export("Выгрузка новостей", newsToExport);
            System.out.printf(
                    "%n<*> Выгрузка новостей успешно завершена%n" +
                            "<*> Имя файла: %s%n%n",
                    fileName
            );
        } catch (IOException e) {
            System.out.println();
            System.out.printf(Errors.EXPORT_ERROR_S.getDefaultMessage() + "%n", e.getMessage());
        }
    }
}
