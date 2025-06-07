package com.example.news_aggregator.repository;

import com.example.news_aggregator.model.logs.LogAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogActionRepository extends JpaRepository<LogAction, Long> {
}
