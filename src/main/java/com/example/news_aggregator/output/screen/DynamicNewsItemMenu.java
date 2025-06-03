package com.example.news_aggregator.output.screen;

import com.example.news_aggregator.common.menu.MenuDescriptor;
import com.example.news_aggregator.common.menu.impl.BaseMenu;
import com.example.news_aggregator.common.menu.impl.MenuBuilder;
import com.example.news_aggregator.model.news.News;
import lombok.Getter;

public class DynamicNewsItemMenu extends BaseMenu {

    private final News newsItem;

    protected DynamicNewsItemMenu(
            MenuDescriptor menuDescriptor,
            News newsItem
    ) {
        super(menuDescriptor);

        this.newsItem = newsItem;
    }

    @Override
    protected void printMenuDescription() {
        System.out.println(newsItem.getTitle());
        printWrapped(newsItem.getContent(), BaseMenu.LINE_WIDTH);

        System.out.println();
    }

    public static class Builder extends MenuBuilder<DynamicNewsItemMenu, DynamicNewsItemMenu.Builder> {

        private News newsItem = null;

        public Builder withNewsItem(News newsItem) {
            this.newsItem = newsItem;
            return this;
        }

        @Override
        protected DynamicNewsItemMenu createMenu() {
            return new DynamicNewsItemMenu(
                    getMenuDescriptor(),
                    newsItem
            );
        }

        @Override
        protected Builder getBuilder() {
            return this;
        }
    }
}
