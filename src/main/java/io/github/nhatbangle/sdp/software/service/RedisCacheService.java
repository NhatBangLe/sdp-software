package io.github.nhatbangle.sdp.software.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisCacheManager cacheManager;

    public void invalidateKey(@NotNull String cacheName, @NotNull String key) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
            log.info("Invalidate cache name {} with key {}.", cacheName, key);
        } else {
            log.warn("Cache with name '{}' and key '{}' not found or is empty.", cacheName, key);
        }
    }

}
