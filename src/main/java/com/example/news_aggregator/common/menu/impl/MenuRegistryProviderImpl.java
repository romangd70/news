package com.example.news_aggregator.common.menu.impl;

import com.example.news_aggregator.common.menu.MenuRegistry;
import com.example.news_aggregator.common.menu.MenuRegistryProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MenuRegistryProviderImpl implements MenuRegistryProvider {

    private MenuRegistry menuRegistry;
}
