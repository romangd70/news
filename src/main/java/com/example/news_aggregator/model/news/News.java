package com.example.news_aggregator.model.news;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "raw_content", columnDefinition = "TEXT", nullable = false)
    private String rawContent;

    @Column(name = "publication_date", nullable = false)
    private LocalDateTime publicationDate;

    @Column(name = "url", nullable = false)
    private String url;

    @JoinColumn(name = "source_id", nullable = false)
    @ManyToOne
    private Source source;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private Category category;

    @ElementCollection
    @CollectionTable(name = "news_keywords", joinColumns = @JoinColumn(name = "news_id", nullable = false))
    @Column(name = "keyword")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "news_media_links", joinColumns = @JoinColumn(name = "news_id", nullable = false))
    @Column(name = "media_link")
    private List<String> mediaLinks;

    @Column(name = "hash")
    private Integer hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(title, news.title)
                && Objects.equals(description, news.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }
}
