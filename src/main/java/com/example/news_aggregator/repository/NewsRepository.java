package com.example.news_aggregator.repository;

import com.example.news_aggregator.model.news.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByHashIn(List<Integer> hash);

    List<News> findAllBySourceId(Integer sourceId);

    List<News> findAllByCategoryId(Integer categoryId);

    List<News> findAllByPublicationDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<News> findAllByKeywords(List<String> keyword);

    @Query("SELECT n.category.name, COUNT(n) FROM News n GROUP BY n.category.name")
    List<Object[]> countNewsByCategory();

    @Query(value = "SELECT keyword, COUNT(keyword) AS frequency " +
            "FROM news_keywords " +
            "GROUP BY keyword " +
            "ORDER BY frequency DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findTopKeywords();

    @Query("SELECT k AS keyword, " +
            "       FUNCTION('DATE_TRUNC', 'day', n.publicationDate) AS period, " +
            "       COUNT(n) AS newsCount " +
            "FROM News n " +
            "JOIN n.keywords k " +
            "WHERE n.publicationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY k, period")
    List<Object[]> findKeywordTrends(LocalDateTime startDate,
                                     LocalDateTime endDate);

}
