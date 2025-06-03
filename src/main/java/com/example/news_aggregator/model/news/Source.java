package com.example.news_aggregator.model.news;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sources")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "news_feed_url")
    private String newsFeedUrl;

    @Column(name = "article_element_selector")
    private String articleElementSelector;

    @Column(name = "article_element_attribute")
    private String articleElementAttribute;

    @Column(name = "category_selector")
    private String categorySelector;

    @Column(name = "category_attribute")
    private String categoryAttribute;

    @Column(name = "title_selector")
    private String titleSelector;

    @Column(name = "title_attribute")
    private String titleAttribute;

    @Column(name = "content_selector")
    private String contentSelector;

    @Column(name = "content_attribute")
    private String contentAttribute;

    @Column(name = "published_at_selector")
    private String publishedAtSelector;

    @Column(name = "published_at_attribute")
    private String publishedAtAttribute;

    @Column(name = "date_time_format")
    private String dateTimeFormat;

    @Column(name = "short_description_selector")
    private String shortDescriptionSelector;

    @Column(name = "short_description_attribute")
    private String shortDescriptionAttribute;

    @Column(name = "keyword_selector")
    private String keywordSelector;

    @Column(name = "keyword_attribute")
    private String keywordAttribute;

    @Column(name = "is_active")
    private Boolean isActive;
}
