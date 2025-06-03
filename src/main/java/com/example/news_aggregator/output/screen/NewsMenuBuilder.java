package com.example.news_aggregator.output.screen;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.menu.Menu;
import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.MenuItemDescriptor;
import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.MenuRegistryProvider;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.common.menu.impl.MenuBuilder;
import com.example.news_aggregator.common.menu.impl.MenuDescriptorImpl;
import com.example.news_aggregator.common.menu.impl.MenuItemDescriptorImpl;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.menu.StaticMenu;
import com.example.news_aggregator.model.news.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class NewsMenuBuilder {

    private final static int NEWS_ON_PAGE_COUNT = 9;
    private final static SortOrder SORT_ORDER = SortOrder.BY_TITLE;

    private final MenuRegistryProvider menuRegistryProvider;

    @Autowired
    public NewsMenuBuilder(
            MenuRegistryProvider menuRegistryProvider
    ) {
        this.menuRegistryProvider = menuRegistryProvider;
    }

    public MenuDescriptor build(
            String title,
            Collection<News> newsToPrint
    ) throws NewsAggregatorIllegalArgumentException {
        // Проверим наличие новостей
        if (newsToPrint.isEmpty()) {
            throw new NewsAggregatorIllegalArgumentException(Errors.NO_NEWS_TO_DISPLAY);
        }

        // Отсортируем по критерию
        List<News> sortedNews = sortNews(newsToPrint);

        // Страничное отображение новостей
        // Каждая страница соответствует своему динамическому меню
        int pageCount = Math.ceilDiv(sortedNews.size(), NEWS_ON_PAGE_COUNT);

        // Сформируем дескрипторы динамических меню для каждой страницы новостей
        // Объекты билдеров меню выступят в роли хранилища дескрипторов
        List<BaseMenu.Builder> menuBuilders = new ArrayList<>();
        for (int currentPageIdx = 0; currentPageIdx < pageCount; currentPageIdx++) {
            String menuId = UUID.randomUUID().toString();
            MenuDescriptor menuDescriptor = new MenuDescriptorImpl(
                    menuId,
                    String.format("%s (страница %s из %s)", title, currentPageIdx + 1, pageCount),
                    true // Динамический дескриптор
            );

            BaseMenu.Builder menuBuilder = new BaseMenu.Builder()
                    .withMenuDescriptor(menuDescriptor);
            menuBuilders.add(menuBuilder);
        }

        // Получим ссылку на первую и последнюю страницу
        MenuDescriptor firstPageMenuDescriptor = menuBuilders.get(0).getMenuDescriptor();
        MenuDescriptor lastPageMenuDescriptor = menuBuilders.get(menuBuilders.size() - 1).getMenuDescriptor();

        // Получение реестра меню через объект провайдера - для исключения перекрестных ссылок
        MenuRegistry menuRegistry = menuRegistryProvider.getMenuRegistry();

        // Теперь формируем элементы каждого динамического меню
        for (int currentPageIdx = 0; currentPageIdx < pageCount; currentPageIdx++) {
            BaseMenu.Builder menuBuilder = menuBuilders.get(currentPageIdx);
            MenuDescriptor currentMenuDescriptor = menuBuilder.getMenuDescriptor();

            // Заполняем элементы меню - список новостей
            int listIdx = currentPageIdx * NEWS_ON_PAGE_COUNT;
            int newsItemStartId = 1;
            for (int newsIdx = 0; newsIdx < NEWS_ON_PAGE_COUNT && listIdx < sortedNews.size(); newsIdx++) {
                News newsItem = sortedNews.get(listIdx++);

                // Создание меню просмотра конкретной новости
                MenuDescriptor newsItemMenuDescriptor = createNewsItemMenu(currentMenuDescriptor, newsItem, "Просмотр новости");
                // Создание переключателя на меню просмотра новости
                createDynamicSwitcher(menuBuilder, newsItem.getTitle(), String.valueOf(newsItemStartId++), newsItemMenuDescriptor);
            }

            // Переход на следующую и предыдущую страницу
            boolean hasNextPage = currentPageIdx < pageCount - 1;
            boolean hasPrevPage = currentPageIdx > 0;
            if (pageCount > 1) {
                String goToNextTitle = hasNextPage
                        ? "Отобразить следующую страницу"
                        : "Отобразить первую страницу";
                MenuDescriptor nextMenuDescriptor = hasNextPage
                        ? menuBuilders.get(currentPageIdx + 1).getMenuDescriptor()
                        : firstPageMenuDescriptor;
                createDynamicSwitcher(menuBuilder, goToNextTitle, "+", nextMenuDescriptor);

                String goToPrevTitle = hasPrevPage
                        ? "Отобразить предыдущую страницу"
                        : "Отобразить последнюю страницу";
                MenuDescriptor prevMenuDescriptor = hasPrevPage
                        ? menuBuilders.get(currentPageIdx - 1).getMenuDescriptor()
                        : lastPageMenuDescriptor;
                createDynamicSwitcher(menuBuilder, goToPrevTitle, "-", prevMenuDescriptor);
            }

            // Пункт выхода из просмотра
            String exitDescriptorId = StaticMenu.toDescriptor(StaticMenu.READ_NEWS).getId();
            MenuDescriptor exitDescriptor = menuRegistry.getMenuDescriptor(exitDescriptorId);
            createDynamicSwitcher(menuBuilder, "Выйти из просмотра", "0", exitDescriptor);
        }

        // Создание и регистрация объектов меню
        menuBuilders.forEach(builder -> {
            Menu menu = builder.build();
            menuRegistry.registerMenu(menu);
        });

        // Возвращаем первый дескриптор меню (первая страница)
        return firstPageMenuDescriptor;
    }

    //region Private helpers
    private List<News> sortNews(Collection<News> newsToPrint) {
        // Произведем сортировку новостей по заданному критерию.
        // Используем анонимную реализацию посетителя.
        return SORT_ORDER.sort(
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
                },
                newsToPrint // Новости, подлежащие сортировке
        );
    }

    private void createDynamicCommand(
            BaseMenu.Builder menuBuilder,
            String title,
            String inputKey
    ) {
        // Создание динамического дескриптора с уникальным идентификатором
        String menuItemId = UUID.randomUUID().toString();
        MenuItemDescriptor menuItemDescriptor = new MenuItemDescriptorImpl(
                menuItemId,
                title,
                true // Динамический дескриптор
        );

        // Регистрация элемента меню в реестре
        MenuRegistry menuRegistry = menuRegistryProvider.getMenuRegistry();
        menuRegistry.registerMenuItem(new DynamicNewsMenuCommand(menuItemDescriptor));

        // Добавление в меню
        menuBuilder.withMenuItem(inputKey, menuItemDescriptor);
    }

    private void createDynamicSwitcher(
            MenuBuilder<?, ?> menuBuilder,
            String title,
            String inputKey,
            MenuDescriptor nextMenuDescriptor
    ) {
        // Создание динамического дескриптора с уникальным идентификатором
        String menuItemId = UUID.randomUUID().toString();
        MenuItemDescriptor menuItemDescriptor = new MenuItemDescriptorImpl(
                menuItemId,
                title,
                true // Динамический дескриптор
        );

        // Регистрация элемента меню в реестре
        MenuRegistry menuRegistry = menuRegistryProvider.getMenuRegistry();
        menuRegistry.registerMenuItem(new DynamicNewsMenuSwitcher(menuItemDescriptor, nextMenuDescriptor));

        // Добавление в меню
        menuBuilder.withMenuItem(inputKey, menuItemDescriptor);
    }

    private MenuDescriptor createNewsItemMenu(
            MenuDescriptor parentMenuDescriptor,
            News newsItem,
            String title
    ) {
        // Создание динамического дескриптора с уникальным идентификатором
        String menuId = UUID.randomUUID().toString();
        MenuDescriptor menuDescriptor = new MenuDescriptorImpl(
                menuId,
                title,
                true // Динамический дескриптор
        );
        // Создание меню просмотра конкретной новости
        DynamicNewsItemMenu.Builder newsItemMenuBuilder = new DynamicNewsItemMenu.Builder()
                .withNewsItem(newsItem)
                .withMenuDescriptor(menuDescriptor);

        // Выход из просмотра новости
        createDynamicSwitcher(newsItemMenuBuilder, "Назад", "0", parentMenuDescriptor);

        // Создание и регистрация объекта меню
        MenuRegistry menuRegistry = menuRegistryProvider.getMenuRegistry();
        menuRegistry.registerMenu(newsItemMenuBuilder.build());

        return menuDescriptor;
    }
    //endregion Private helpers

    /**
     * Тип сортировки при выводе новостей.
     * Для обработки значения перечисления использован паттерн Visitor.
     * Подобная реализация позволяет надежно обработать все значения перечисления, если значение добавится или убавится,
     * так как ошибка появится во время компиляции.
     * Кроме этого, каждое значение будет гарантировано обработано.
     */
    public enum SortOrder {
        BY_DATE {
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
        };

        public abstract List<News> sort(SortOrderVisitor visitor, Collection<News> sourceNews);

        public interface SortOrderVisitor {
            List<News> visitByDate(Collection<News> sourceNews);

            List<News> visitByTitle(Collection<News> sourceNews);
        }
    }
}
