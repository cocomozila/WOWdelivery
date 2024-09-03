package com.wow.delivery.service;

import com.wow.delivery.dto.shop.ShopResponse;
import com.wow.delivery.dto.shop.ShopUpdateDTO;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.BusinessHours;
import com.wow.delivery.entity.shop.S2LevelToken;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.repository.ShopRepository;
import com.wow.delivery.service.shop.ShopService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.time.DayOfWeek;
import java.util.List;

@SpringBootTest
class ShopServiceCacheTest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        ShopEntity shopEntity = ShopEntity.builder()
            .ownerId(1L)
            .shopName("일산 맛집식당")
            .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
            .businessHours(BusinessHours.builder()
                .openTime("11:30")
                .closeTime("21:00")
                .build())
            .minOrderPrice(12000)
            .deliveryFee(3000)
            .address(Address.builder()
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("화정로")
                .buildingNumber("53")
                .addressDetail("102호")
                .latitude(126.8319146)
                .longitude(37.6351911)
                .build())
            .openDays(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
            .s2LevelToken(new S2LevelToken(126.8319146, 37.6351911))
            .build();

        shopEntity.setId(1L);

        shopRepository.save(shopEntity);
    }

    @Test
    @DisplayName("일반 조회 - 캐시에 이미 데이터가 있음")
    void success_cache_search() {
        // when
        ShopResponse shopResponse = shopService.getShop(1L);

        // then
        ShopEntity shopEntityCache = cacheManager.getCache("shopEntityCache").get(1L, ShopEntity.class);
        Assertions.assertThat(shopEntityCache.getShopName()).isEqualTo(shopResponse.getShopName());
    }

    @Test
    @DisplayName("캐시 수정 시 캐시가 삭제됨")
    void cache_delete() {
        // given
        ShopResponse shopResponse = shopService.getShop(1L);

        ShopUpdateDTO updateDTO = ShopUpdateDTO.builder()
            .shopId(1L)
            .shopName("수정된 식당")
            .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
            .openDays(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
            .openTime("11:30")
            .closeTime("21:00")
            .minOrderPrice(12000)
            .deliveryFee(3000)
            .state("경기도")
            .city("고양시")
            .district("덕양구")
            .streetName("화정로")
            .buildingNumber("53")
            .addressDetail("102호")
            .latitude(126.8319146)
            .longitude(37.6351911)
            .build();

        // when
        shopService.updateShop(updateDTO);

        // then
        ShopEntity shopEntityCache = cacheManager.getCache("shopEntityCache").get(1L, ShopEntity.class);
        Assertions.assertThat(shopEntityCache).isNull();

    }
}