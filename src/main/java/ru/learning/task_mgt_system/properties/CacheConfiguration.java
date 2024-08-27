package ru.learning.task_mgt_system.properties;

import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up caching in the Spring Boot application.
 * This class configures the cache manager based on properties defined in AppCacheProperties.
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {

    /**
     * Defines a bean for ConcurrentMapCacheManager.
     * This bean is created only if the 'app.cache.cacheType' property is set to 'inMemory'.
     *
     * @param appCacheProperties The application cache properties.
     * @return An instance of ConcurrentMapCacheManager.
     */
    @Bean
    @ConditionalOnExpression("'${app.cache.cacheType}'.equals('inMemory')")
    public ConcurrentMapCacheManager inMemoryCacheManager(AppCacheProperties appCacheProperties) {
        // Create a custom ConcurrentMapCacheManager
        var cacheManager = new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                // Create a ConcurrentMapCache with custom configuration
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(appCacheProperties.getCaches().get(name).getExpiry())
                                .build()
                                .asMap(),
                        true
                );
            }
        };

        // Set cache names if specified in properties
        var cacheNames = appCacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }
        return cacheManager;
    }
}