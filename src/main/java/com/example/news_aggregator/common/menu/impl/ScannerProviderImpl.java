package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.ScannerProvider;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Scanner;

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
