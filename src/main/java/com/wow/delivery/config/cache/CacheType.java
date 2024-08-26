package com.wow.delivery.config.cache;

import lombok.Getter;

@Getter
public enum CacheType {

    GET_SHOP_CACHE("getShopCache", null, null),
    SHOP_ENTITY_CACHE("shopEntityCache", null, null);

    private final String name;
    private final int expireAfterWrite;
    private final int maximumSize;

    CacheType(String name, Integer expireAfterWrite, Integer maximumSize) {
        this.name = name;
        if (expireAfterWrite != null) {
            this.expireAfterWrite = expireAfterWrite;
        } else {
            this.expireAfterWrite = ConstConfig.DEFAULT_TTL_SEC;
        }
        if (maximumSize != null) {
            this.maximumSize = maximumSize;
        } else {
            this.maximumSize = ConstConfig.DEFAULT_MAX_SIZE;
        }
    }

    static class ConstConfig {
        static final int DEFAULT_TTL_SEC = 60 * 3;
        static final int DEFAULT_MAX_SIZE = 10000;
    }
}
