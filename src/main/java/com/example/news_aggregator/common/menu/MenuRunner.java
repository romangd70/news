package com.example.news_aggregator.common.menu;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.exception.NewsAggregatorIllegalStateException;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.enums.Texts;
import com.example.news_aggregator.menu.StaticMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Объект реализует главный цикл обработки пользовательского ввода.
 * Фактически, пока работает цикл обработки, работает и программа.
 */
@Component
public class MenuRunner {

    private final MenuRegistry menuRegistry;
    private final ScannerProvider scannerProvider;

    /**
     * Spring автоматически обеспечит наличие зависимостей, указанных как параметры конструктора.
     *
     * @param menuRegistry    Реестр меню для получения меню и его элементов.
     * @param scannerProvider Провайдер сканера для обработки пользовательского ввода.
     */
    @Autowired
    public MenuRunner(
            MenuRegistry menuRegistry,
            ScannerProvider scannerProvider
    ) {
        this.menuRegistry = menuRegistry;
        this.scannerProvider = scannerProvider;
    }

    /**
     * Точка входа в цикл обработки пользовательского ввода.
     * Этот метод создан для удобства работы со статическим меню.
     * Фактически осуществляет перенаправление в аналогичный метод, но с дескриптором меню.
     *
     * @param staticMenu Ссылка на описание статического меню.
     */
    public void run(StaticMenu staticMenu) {
        run(staticMenu.name());
    }

    /**
     * Точка входа в цикл обработки пользовательского ввода.
     * Осуществляет отображение меню, запуск обработки выбора элементов меню.
     * Осуществляет перехват ошибок и вывод их на экран.
     *
     * @param startMenuId Идентификатор стартового меню.
     */
    public void run(String startMenuId) {
        Scanner scanner = scannerProvider.getScanner();
        Menu currentMenu = menuRegistry.getMenu(startMenuId);

        String inputKey;
        do {
            // Отображаем содержимое текущего меню
            currentMenu.print();

            // Вывод приглашения пользователю на ввод команды и ожидание ввода
            System.out.print(Texts.CHOOSE_MENU_ITEM.getDefaultMessage());
            while (!scanner.hasNextLine()) {
                scanner.next();
            }
            inputKey = scanner.nextLine();

            // Попытка получения элемента меню по введенному ключу
            // В случае неудачной попытки выводим сообщение об ошибке и повторяем цикл заново
            MenuItem menuItem;
            try {
                menuItem = currentMenu.getMenuItem(inputKey);
            } catch (NewsAggregatorNotFoundException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // Анализ типа элемента меню
            // Разные типы обрабатываются по разному
            try {
                if (menuItem instanceof TerminateApplicationCommand terminateApplicationCommand) {
                    // Позволяем команде финализировать приложение, если необходимо
                    terminateApplicationCommand.execute(scanner);
                    // На этом цикл обработки должен быть прерван, приложение завершит работу
                    break;
                } else if (menuItem instanceof MenuSwitcher menuSwitcher) {
                    // Позволим переключателю выполнить необходимые действия
                    // В случае переключателя на динамическое меню это могут быть операции по созданию динамического меню
                    menuSwitcher.execute(scanner);
                    // Теперь попросим целевое меню для перехода
                    String nextMenuId = menuSwitcher.getNextMenuId();
                    currentMenu = menuRegistry.getMenu(nextMenuId);
                } else if (menuItem instanceof MenuCommand menuCommand) {
                    menuCommand.execute(scanner);
                } else {
                    // Это исключение не обрабатывается внешним перехватчиком и приведет к аварийному завершению программы
                    // Нам это и нужно, так как код по обработке нужного элемента меню отсутствует
                    throw new NewsAggregatorIllegalStateException(
                            Errors.UNSUPPORTED_MENU_ITEM_TYPE_S, menuItem.getClass().getSimpleName()
                    );
                }
            } catch (NewsAggregatorIllegalStateException | NewsAggregatorNotFoundException | NewsAggregatorIllegalArgumentException e) {
                // Выводим сообщение об ошибке и уходим на новый круг
                System.out.println(e.getMessage());
            }
        } while (inputKey != null);
    }
}