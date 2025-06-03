package com.example.news_aggregator.enums;

import com.example.news_aggregator.common.EnumWithId;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Errors implements EnumWithId {

    LOG_ACTION_TYPE_ID_S_NOT_FOUND(1, "Тип лога с идентификатором [%s] не найден."),
    UNSUPPORTED_LOG_ACTION_TYPE_ID_S(2, "Тип лога с идентификатором [%s] не поддерживается."),
    UNABLE_TO_CONNECT_SOURCE_S(3, "Ошибка подключения к источнику: [%s]."),
    ERROR_PARSING_ARTICLE_S(4, "Ошибка во время парсинга статьи [%s]."),
    NO_ARTICLES_FOUND_FOR_SOURCE_NAME_S(5, "В источнике [%s] не найдено статей. Проверьте правильность указанных селекторов и аттрибутов в базе данных."),
    CATEGORY_ID_S_NOT_FOUND(6, "Категория с идентификатором [%s] не найдена."),

    // Ошибки отображения меню
    UNKNOWN_MENU_KEY(101, "Неверный выбор."),
    UNSUPPORTED_MENU_ITEM_TYPE_S(102, "Неподдерживаемый тип элемента меню: [%s]."),
    MENU_WITH_ID_S_TITLE_S_NOT_FOUND(103, "Реализация меню [%s - %s] не найдена."),
    MENU_ITEM_WITH_ID_S_TITLE_S_NOT_FOUND(104, "Реализация элемента меню [%s - %s] не найдена."),
    MENU_DESCRIPTOR_WITH_ID_S_NOT_FOUND(105, "Дескриптор меню [%s] не найден."),
    MENU_ITEM_DESCRIPTOR_WITH_ID_S_NOT_FOUND(106, "Дескриптор элемента меню [%s] не найден."),

    // Ошибки отображения новостей
    NO_NEWS_TO_DISPLAY(200, "Нет новостей для отображения."),

    // Обработки ошибок ввода
    TWO_DATES_NOT_SET(301, "Необходимо ввести две даты через пробел."),
    ILLEGAL_DATE_ORDER(302, "Начальная дата должна быть меньше или равна конечной."),
    ILLEGAL_DATE_FORMAT(303, "Неверный формат даты. Пожалуйста, используйте формат dd-MM-yyyy."),
    UNHANDLED_ERROR(304, "Ошибка: [%s]");

    private final int id;
    private final String defaultMessage;

    Errors(int id, String defaultMessage) {
        this.id = id;
        this.defaultMessage = defaultMessage;
    }

    public static Errors getById(int id) {
        return Arrays.stream(Errors.values())
                .filter(error -> error.getId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
