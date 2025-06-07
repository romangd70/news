package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.ScannerProvider;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Реализация провайдера сканера.
 * Включает реализацию интерфейса ScannerProvider посредством аннотации @Getter из Lombok.
 * Осуществляет финализацию объекта сканера в методе preDestroy().
 */
@Getter
@Component
public class ScannerProviderImpl implements ScannerProvider {

    private final Scanner scanner;

    public ScannerProviderImpl() {
        this.scanner = new Scanner(System.in);
    }

    @PreDestroy
    public void preDestroy() {
        scanner.close();
    }
}
