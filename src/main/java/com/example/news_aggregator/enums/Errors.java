package com.example.news_aggregator.enums;

import com.example.news_aggregator.common.EnumWithId;
import lombok.Getter;

@Getter
public enum Errors implements EnumWithId {

    LOG_ACTION_TYPE_ID_S_NOT_FOUND(1, "Тип лога с идентификатором [%s] не найден."),
    UNSUPPORTED_LOG_ACTION_TYPE_ID_S(2, "Тип лога с идентификатором [%s] не поддерживается."),
    UNABLE_TO_CONNECT_SOURCE_S(3, "Ошибка подключения к источнику: [%s]."),
    ERROR_PARSING_ARTICLE_S(4, "Ошибка во время парсинга статьи [%s]."),
    NO_ARTICLES_FOUND_FOR_SOURCE_NAME_S(5, "В источнике [%s] не найдено статей. Проверьте правильность указанных селекторов и аттрибутов в базе данных."),
    CATEGORY_ID_S_NOT_FOUND(6, "Категория с идентификатором [%s] не найдена."),
    SETTING_TYPE_ID_S_NOT_FOUND(7, "Тип настройки с идентификатором [%s] не найден."),
    UNSUPPORTED_SETTING_TYPE_ID_S(8, "Тип настройки с идентификатором [%s] не поддерживается."),
    SORT_ORDER_WITH_ID_S_NOT_FOUND(9, "Порядок сортировки с идентификатором [%s] не найден."),

    // Ошибки отображения меню
    UNKNOWN_MENU_KEY(101, "Неверный выбор."),
    UNSUPPORTED_MENU_ITEM_TYPE_S(102, "Неподдерживаемый тип элемента меню: [%s]."),
    MENU_WITH_ID_S_NOT_FOUND(103, "Реализация меню с идентификатором [%s] не найдена."),
    MENU_ITEM_WITH_ID_S_NOT_FOUND(104, "Реализация элемента меню с идентификатором [%s] не найдена."),

    // Ошибки отображения новостей
    NO_NEWS_TO_DISPLAY(200, "Нет новостей для отображения."),

    // Обработки ошибок ввода
    TWO_DATES_NOT_SET(301, "Необходимо ввести две даты через пробел."),
    ILLEGAL_DATE_ORDER(302, "Начальная дата должна быть меньше или равна конечной."),
    ILLEGAL_DATE_FORMAT(303, "Неверный формат даты. Пожалуйста, используйте формат dd-MM-yyyy."),
    UNHANDLED_ERROR_S(304, "Ошибка: [%s]"),
    INVALID_CRON_EXPRESSION(305, "Неверный формат cron-выражения."),
    INVALID_NUMBER_FORMAT(306, "Неверный формат. Введите цифру. 1 - По дате, 2 - По заголовку, 3 - По источнику"),

    // Ошибки экспорта
    EXPORT_ERROR_S(400, "Не удалось экспортировать данные: [%s]");

    private final int id;
    private final String defaultMessage;

    Errors(int id, String defaultMessage) {
        this.id = id;
        this.defaultMessage = defaultMessage;
    }
}
