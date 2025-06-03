package com.example.news_aggregator.repository;

import com.example.news_aggregator.model.news.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends JpaRepository<Source, Integer> {

    List<Source> findAllByIsActive(boolean isActive);
}
