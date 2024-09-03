package com.wow.delivery.service.shop;

import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopCacheService {

    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    @Cacheable(
        key = "#shopId",
        value = "shopEntityCache",
        cacheManager = "redisCacheManager"
    )
    public ShopEntity findByShopIdOrThrow(Long shopId) {
        return shopRepository.findByIdOrThrow(shopId, ErrorCode.SHOP_DATA_NOT_FOUND, null);
    }

    @CacheEvict(
        key = "#shop.id",
        value = "shopEntityCache",
        cacheManager = "redisCacheManager"
    )
    @Transactional
    public void saveShop(ShopEntity shop) {
        shopRepository.save(shop);
    }
}
