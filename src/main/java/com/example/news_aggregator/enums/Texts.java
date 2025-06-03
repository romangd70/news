package com.example.news_aggregator.enums;

import com.example.news_aggregator.common.EnumWithId;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Texts implements EnumWithId {

    // Menu messages
    CHOOSE_MENU_ITEM(100, "Выберите пункт: ");

    private final int id;
    private final String defaultMessage;

    Texts(int id, String defaultMessage) {
        this.id = id;
        this.defaultMessage = defaultMessage;
    }

    public static Texts getById(int id) {
        return Arrays.stream(Texts.values())
                .filter(text -> text.getId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);

    }
}
