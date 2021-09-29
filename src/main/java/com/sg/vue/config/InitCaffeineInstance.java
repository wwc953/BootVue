package com.sg.vue.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class InitCaffeineInstance {
    @Value("${level1.cache.initial.capacity:10000}")
    private int initialCapacity;
    @Value("${level1.cache.maximum.size:20000}")
    private int maximumSize;

    @Bean
    public Cache<String, Object> baseCache() {
        log.info("============== Init CaffeineCache ==============");
        Cache<String, Object> baseCache = Caffeine.newBuilder().initialCapacity(initialCapacity).maximumSize(maximumSize).build();
        log.info("===> CaffeineCache => initialCapacity:{}, maximumSize:{}", initialCapacity, maximumSize);
        return baseCache;
    }
}
