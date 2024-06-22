package com.wow.delivery.service;

import com.wow.delivery.dto.shop.*;
import com.wow.delivery.entity.Owner;
import com.wow.delivery.entity.User;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.BusinessHours;
import com.wow.delivery.entity.shop.S2LevelToken;
import com.wow.delivery.entity.shop.Shop;
import com.wow.delivery.repository.MetaCategoryRepository;
import com.wow.delivery.repository.OwnerRepository;
import com.wow.delivery.repository.ShopCategoryRepository;
import com.wow.delivery.repository.ShopRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @InjectMocks
    private ShopService shopService;

    @Spy
    private ShopRepository shopRepository;

    @Spy
    private ShopCategoryRepository shopCategoryRepository;

    @Spy
    private MetaCategoryRepository metaCategoryRepository;

    @Spy
    private S2Service s2Service;

    @Spy
    private OwnerRepository ownerRepository;

    @Nested
    @DisplayName("생성")
    class create {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Owner owner = Owner.builder()
                .email("owner@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            owner.setId(1L);

            ShopCreateDTO shopCreateDTO = ShopCreateDTO.builder()
                .ownerId(1L)
                .categoryNames(List.of("버거, 커피"))
                .shopName("일산 맛집식당")
                .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
                .openDays(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .openTime("11:30")
                .closeTime("21:00")
                .minOrderPrice(12000)
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("화정로")
                .buildingNumber("53")
                .addressDetail("102호")
                .latitude(126.8319146)
                .longitude(37.6351911)
                .build();

            given(ownerRepository.findById(anyLong())).willReturn(Optional.of(owner));

            // when
            shopService.createShop(shopCreateDTO);

            // then
            then(shopRepository)
                .should(times(1))
                .save(any());
            then(shopCategoryRepository)
                .should(times(1))
                .saveAll(any());
        }
    }

    @Nested
    @DisplayName("조회")
    class Get {

        @Test
        @DisplayName("일반 지역 - 근처 카테고리로 검색 성공")
        void success_category_search() {
            // given
            User user = User.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            Shop shop = Shop.builder()
                .ownerId(1L)
                .shopName("일산 맛집식당")
                .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
                .businessHours(BusinessHours.builder()
                    .openTime("11:30")
                    .closeTime("21:00")
                    .build())
                .minOrderPrice(12000)
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

            CategoryNearbyShopRequestDTO requestDTO = CategoryNearbyShopRequestDTO.categoryNearbyShopRequestDTOBuilder()
                .userId(user.getId())
                .searchCategory("버거")
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("호국로")
                .buildingNumber("754")
                .addressDetail("000동 000호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            given(shopRepository.findByCategoryNearbyShopLevel12(any(), any()))
                .willReturn(List.of(shop));

            // when
            List<NearbyShopResponse> shops = shopService.getShopsByCategory(requestDTO);

            // then
            Assertions.assertThat(shops.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("일반 지역 - 근처 이름으로 검색 성공")
        void success_name_search() {
            // given
            User user = User.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            Shop shop = Shop.builder()
                .ownerId(1L)
                .shopName("일산 맛집식당")
                .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
                .businessHours(BusinessHours.builder()
                    .openTime("11:30")
                    .closeTime("21:00")
                    .build())
                .minOrderPrice(12000)
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

            NameNearbyShopRequestDTO requestDTO = NameNearbyShopRequestDTO.nameNearbyShopRequestDTOBuilder()
                .userId(user.getId())
                .searchShopName("일산")
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("호국로")
                .buildingNumber("754")
                .addressDetail("000동 000호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            given(shopRepository.findByShopNameNearbyShopLevel12(any(), any()))
                .willReturn(List.of(shop));

            // when
            List<NearbyShopResponse> shops = shopService.getShopsByShopName(requestDTO);

            // then
            Assertions.assertThat(shops.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("수정")
    class update {

        @Test
        @DisplayName("성공")
        void success_update() {
            Shop shop = Shop.builder()
                .ownerId(1L)
                .shopName("일산 맛집식당")
                .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
                .businessHours(BusinessHours.builder()
                    .openTime("11:30")
                    .closeTime("21:00")
                    .build())
                .minOrderPrice(12000)
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

            given(shopRepository.findById(anyLong())).willReturn(Optional.of(shop));

            ShopUpdateDTO shopUpdateDTO = ShopUpdateDTO.builder()
                .shopId(1L)
                .categoryNames(List.of("버거, 커피"))
                .shopName("대박식당")
                .introduction("주말에 커피와 간식을 즐길 수 있는 아늑한 장소입니다.")
                .openDays(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .openTime("11:30")
                .closeTime("21:00")
                .minOrderPrice(17000)
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
            shopService.updateShop(shopUpdateDTO);

            // then
            Assertions.assertThat(shop.getShopName()).isEqualTo("대박식당");
            Assertions.assertThat(shop.getMinOrderPrice()).isEqualTo(17000);

        }
    }
}