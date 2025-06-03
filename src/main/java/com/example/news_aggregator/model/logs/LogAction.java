package com.example.news_aggregator.model.logs;

import com.example.news_aggregator.enums.LogActionType;
import com.example.news_aggregator.utils.converter.LogActionTypeAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log_actions")
public class LogAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_id", nullable = false)
    @Convert(converter = LogActionTypeAttributeConverter.class)
    private LogActionType type;

    @Column(name = "target_object_id", nullable = false)
    private Long targetObjectId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
