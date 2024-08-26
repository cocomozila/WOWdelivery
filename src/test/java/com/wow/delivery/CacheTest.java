package com.wow.delivery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
public class CacheTest {

    @Autowired
    private CacheManager cacheManager;  // Bean 주입

    @Test
    public void getAllCaches() {

        for (String cacheName : cacheManager.getCacheNames()) {
            System.out.println("cacheName = " + cacheName);
        }
    }
}
