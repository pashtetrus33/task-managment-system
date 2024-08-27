package ru.learning.task_mgt_system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for holding application cache configuration properties.
 * The properties will be loaded from the configuration file with the prefix "app.cache".
 */
@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {

    /**
     * The type of cache used in the application.
     */
    private CacheType cacheType;

    /**
     * List of cache names that will be used.
     */
    private final List<String> cacheNames = new ArrayList<>();

    /**
     * Map where the key is the cache name and the value is the properties for that cache.
     */
    private final Map<String, CacheProperties> caches = new HashMap<>();

    /**
     * Nested class for holding properties of a specific cache.
     */
    @Data
    public static class CacheProperties {
        /**
         * The duration for which the cache entries are valid.
         */
        private Duration expiry = Duration.ZERO;
    }

    /**
     * Interface for holding cache names constants.
     */
    public interface CacheNames {
        String DATABASE_ENTITIES = "databaseEntities";
        String DATABASE_ENTITY_BY_NAME = "databaseEntityById";
    }

    /**
     * Enumeration for cache types.
     */
    public enum CacheType {
        IN_MEMORY
    }
}