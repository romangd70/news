package com.example.news_aggregator.common.menu;

import java.util.Scanner;

/**
 * Провайдер сканера.
 * Позволяет работать с одним экземпляром сканера во всех компонентах системы.
 */
public interface ScannerProvider {
    Scanner getScanner();
}