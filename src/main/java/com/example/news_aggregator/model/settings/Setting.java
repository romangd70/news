package com.example.news_aggregator.model.settings;

import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.utils.converter.SettingTypeAttributeConverter;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type_id", nullable = false)
    @Convert(converter = SettingTypeAttributeConverter.class)
    private SettingType type;

    @Column(name = "value", nullable = false)
    private String value;
}
