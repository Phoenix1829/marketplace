package ru.phoenix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            org.springframework.data.redis.connection.RedisConnectionFactory factory) {

        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> configs = new HashMap<>();

        configs.put("products",
                defaultConfig.entryTtl(Duration.ofMinutes(5)));

        configs.put("product",
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        configs.put("users",
                defaultConfig.entryTtl(Duration.ofMinutes(5)));

        configs.put("user",
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configs)
                .build();
    }
}