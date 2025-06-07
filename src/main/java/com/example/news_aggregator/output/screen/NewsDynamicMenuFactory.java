package com.example.news_aggregator.output.screen;

import com.example.news_aggregator.common.EnumWithId;
import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuCommand;
import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.common.menu.impl.BaseMenuSwitcher;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.model.news.News;
import com.example.news_aggregator.output.xlsx.ExportNewsToXlsxCommand;
import com.example.news_aggregator.output.xlsx.NewsXlsxExporter;
import com.example.news_aggregator.service.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class NewsDynamicMenuFactory {

    /**
     * Каждой новости будет назначена своя цифра 1..9, ноль используем для команды возврата.
     * Но ничто не мешает нам задать любое число.
     */
    private final static int NEWS_ON_PAGE_COUNT = 9;
    private final static SortOrder DEFAULT_SORT_ORDER = SortOrder.BY_TITLE;

    private final MenuRegistry menuRegistry;
    private final SettingService settingService;
    private final NewsXlsxExporter newsXlsxExporter;

    @Autowired
    public NewsDynamicMenuFactory(
            MenuRegistry menuRegistry,
            SettingService settingService,
            NewsXlsxExporter newsXlsxExporter
    ) {
        this.menuRegistry = menuRegistry;
        this.settingService = settingService;
        this.newsXlsxExporter = newsXlsxExporter;
    }

    /**
     * Создает иерархию динамических меню для отображения списка новостей.
     * Список новостей будет разбит на страницы.
     * Каждой странице соответствует своё меню.
     *
     * @param title       Общий заголовок отображаемых страниц меню.
     * @param newsToPrint Список новостей к отображению
     * @return Идентификатор меню для первой страницы.
     * @throws NewsAggregatorIllegalArgumentException В случае пустого списка новостей.
     */
    public String create(
            String title,
            Collection<News> newsToPrint
    ) throws NewsAggregatorIllegalArgumentException {
        // Проверим наличие новостей
        if (newsToPrint.isEmpty()) {
            throw new NewsAggregatorIllegalArgumentException(Errors.NO_NEWS_TO_DISPLAY);
        }

        // Отсортируем по критерию
        List<News> sortedNews = sortNews(newsToPrint);

        // Команда экспорта всех новостей к отображению
        MenuCommand exportNewsCommand = createExportNewsCommand(
                "Выгрузка новостей в Excel файл",
                sortedNews
        );

        // Страничное отображение новостей
        // Каждая страница соответствует своему динамическому меню
        int pageCount = Math.ceilDiv(sortedNews.size(), NEWS_ON_PAGE_COUNT);

        // Сформируем динамические меню для каждой страницы новостей
        List<BaseMenu> menus = new ArrayList<>();
        for (int currentPageIdx = 0; currentPageIdx < pageCount; currentPageIdx++) {
            String menuId = UUID.randomUUID().toString();
            BaseMenu menu = new BaseMenu(
                    menuId,
                    String.format("%s (страница %s из %s)", title, currentPageIdx + 1, pageCount),
                    true, // Динамический дескриптор
                    menuRegistry
            );
            menus.add(menu);

            // Регистрация динамического меню в реестре
            menuRegistry.registerMenu(menu);
        }

        // Получим ссылку на первую и последнюю страницу
        Menu firstPageMenu = menus.get(0);
        Menu lastPageMenu = menus.get(menus.size() - 1);

        // Теперь формируем элементы каждого динамического меню
        for (int currentPageIdx = 0; currentPageIdx < pageCount; currentPageIdx++) {
            BaseMenu currentMenu = menus.get(currentPageIdx);

            // Заполняем элементы меню - список новостей
            int listIdx = currentPageIdx * NEWS_ON_PAGE_COUNT;
            int newsItemStartId = 1;
            for (int newsIdx = 0; newsIdx < NEWS_ON_PAGE_COUNT && listIdx < sortedNews.size(); newsIdx++) {
                News newsItem = sortedNews.get(listIdx++);

                // Создание меню просмотра конкретной новости
                String newsItemMenuId = createNewsItemMenu(currentMenu.getId(), newsItem, "Просмотр новости");
                // Создание переключателя на меню просмотра новости
                createDynamicSwitcher(currentMenu, newsItem.getTitle(), String.valueOf(newsItemStartId++), newsItemMenuId);
            }

            // Переход на следующую и предыдущую страницу
            boolean hasNextPage = currentPageIdx < pageCount - 1;
            boolean hasPrevPage = currentPageIdx > 0;
            if (pageCount > 1) {
                String goToNextTitle = hasNextPage
                        ? "Отобразить следующую страницу"
                        : "Отобразить первую страницу";
                Menu nextMenu = hasNextPage ? menus.get(currentPageIdx + 1) : firstPageMenu;
                createDynamicSwitcher(currentMenu, goToNextTitle, "+", nextMenu.getId());

                String goToPrevTitle = hasPrevPage
                        ? "Отобразить предыдущую страницу"
                        : "Отобразить последнюю страницу";
                Menu prevMenu = hasPrevPage ? menus.get(currentPageIdx - 1) : lastPageMenu;
                createDynamicSwitcher(currentMenu, goToPrevTitle, "-", prevMenu.getId());

                // На каждую страницу добавим команду выгрузки в xlsx
                // Выгружаем все новости, вне зависимости от размера страницы
                currentMenu.addMenuItem("xlsx", exportNewsCommand.getId());
            }

            // Пункт выхода из просмотра
            createDynamicSwitcher(currentMenu, "Выйти из просмотра", "0", StaticMenu.READ_NEWS.name());
        }

        // Возвращаем идентификатор первого меню (первая страница)
        return firstPageMenu.getId();
    }

    //region Private helpers
    private List<News> sortNews(Collection<News> newsToPrint) {
        // Пытаемся найти в настройках порядок сортировки
        String settingValue = settingService.getSettingValueById(SettingType.SORTING_TYPE.getId());
        SortOrder sortOrder;
        try {
            sortOrder = SortOrder.getById(Integer.parseInt(settingValue));
        } catch (Exception e) {
            // Если не находим, используем порядок по умолчанию
            sortOrder = DEFAULT_SORT_ORDER;
        }
        // Произведем сортировку новостей по заданному критерию.
        // Используем анонимную реализацию посетителя.
        return sortOrder.sort(
                // При необходимости можно вынести в конкретную реализацию и использовать в разных местах
                new SortOrder.SortOrderVisitor() {
                    @Override
                    public List<News> visitByDate(Collection<News> sourceNews) {
                        if (CollectionUtils.isEmpty(sourceNews)) {
                            return Collections.emptyList();
                        }
                        // Сортировка по дате и добавление в список назначения
                        return sourceNews.stream()
                                .sorted(Comparator.comparing(News::getPublicationDate))
                                .toList();
                    }

                    @Override
                    public List<News> visitByTitle(Collection<News> sourceNews) {
                        if (CollectionUtils.isEmpty(sourceNews)) {
                            return Collections.emptyList();
                        }
                        // Сортировка по заголовку и добавление в список назначения
                        return sourceNews.stream()
                                .sorted(Comparator.comparing(News::getTitle))
                                .toList();
                    }

                    @Override
                    public List<News> visitBySource(Collection<News> sourceNews) {
                        if (CollectionUtils.isEmpty(sourceNews)) {
                            return Collections.emptyList();
                        }
                        // Сортировка по источнику и добавление в список назначения
                        return sourceNews.stream()
                                .sorted(Comparator.comparing(news -> news.getSource().getId()))
                                .toList();
                    }
                },
                newsToPrint // Новости, подлежащие сортировке
        );
    }

    private void createDynamicSwitcher(
            BaseMenu menu,
            String title,
            String inputKey,
            String nextMenuId
    ) {
        // Создание динамического элемента меню с уникальным идентификатором
        String menuItemId = UUID.randomUUID().toString();
        BaseMenuSwitcher menuSwitcher = new BaseMenuSwitcher(
                menuItemId,
                title,
                nextMenuId
        );

        // Регистрация элемента меню в реестре
        menuRegistry.registerMenuItem(menuSwitcher);

        // Добавление в меню
        menu.addMenuItem(inputKey, menuItemId);
    }

    private String createNewsItemMenu(
            String parentMenuId,
            News newsItem,
            String title
    ) {
        // Создание меню просмотра конкретной новости
        String menuId = UUID.randomUUID().toString();
        DynamicNewsItemMenu newsItemMenu = new DynamicNewsItemMenu(
                menuId,
                title,
                menuRegistry,
                newsItem
        );

        // Команда экспорта новости в xlsx
        MenuCommand exportNewsItemCommand = createExportNewsCommand(
                "Выгрузка новости в Excel файл",
                List.of(newsItem)
        );
        newsItemMenu.addMenuItem("xlsx", exportNewsItemCommand.getId());

        // Выход из просмотра новости
        createDynamicSwitcher(newsItemMenu, "Назад", "0", parentMenuId);

        // Регистрация динамического меню в реестре
        menuRegistry.registerMenu(newsItemMenu);

        return menuId;
    }

    private MenuCommand createExportNewsCommand(
            String title,
            List<News> newsToExport
    ) {
        String menuItemId = UUID.randomUUID().toString();
        ExportNewsToXlsxCommand result = new ExportNewsToXlsxCommand(
                menuItemId,
                title,
                newsToExport,
                newsXlsxExporter
        );
        // Регистрация элемента меню в реестре
        menuRegistry.registerMenuItem(result);

        return result;
    }
    //endregion Private helpers

    /**
     * Тип сортировки при выводе новостей.
     * Для обработки значения перечисления использован паттерн Visitor.
     * Подобная реализация позволяет надежно обработать все значения перечисления, если значение добавится или убавится,
     * так как ошибка появится во время компиляции.
     * Кроме этого, каждое значение будет гарантировано обработано.
     */
    public enum SortOrder implements EnumWithId {
        BY_DATE {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public List<News> sort(SortOrderVisitor visitor, Collection<News> sourceNews) {
                return visitor.visitByDate(sourceNews);
            }


        },
        BY_TITLE {
            @Override
            public List<News> sort(SortOrderVisitor visitor, Collection<News> sourceNews) {
                return visitor.visitByTitle(sourceNews);
            }

            @Override
            public int getId() {
                return 2;
            }
        },
        BY_SOURCE {
            @Override
            public List<News> sort(SortOrderVisitor visitor, Collection<News> sourceNews) {
                return visitor.visitBySource(sourceNews);
            }

            @Override
            public int getId() {
                return 3;
            }
        };

        public static SortOrder getById(int id) {
            return Arrays.stream(SortOrder.values())
                    .filter(sortOrder -> sortOrder.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new NewsAggregatorNotFoundException(Errors.SORT_ORDER_WITH_ID_S_NOT_FOUND, id));
        }

        public abstract List<News> sort(SortOrderVisitor visitor, Collection<News> sourceNews);

        public interface SortOrderVisitor {
            List<News> visitByDate(Collection<News> sourceNews);

            List<News> visitByTitle(Collection<News> sourceNews);

            List<News> visitBySource(Collection<News> sourceNews);
        }
    }
}
