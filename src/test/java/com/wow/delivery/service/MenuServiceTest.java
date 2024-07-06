package com.wow.delivery.service;

import com.wow.delivery.dto.menu.MenuCreateForm;
import com.wow.delivery.dto.menu.MenuOrderUpdateDTO;
import com.wow.delivery.dto.menu.MenuResponse;
import com.wow.delivery.dto.menu.MenuUpdateForm;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.menu.Menu;
import com.wow.delivery.entity.shop.BusinessHours;
import com.wow.delivery.entity.shop.S2LevelToken;
import com.wow.delivery.entity.shop.Shop;
import com.wow.delivery.repository.MenuRepository;
import com.wow.delivery.repository.ShopRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Spy
    private MenuRepository menuRepository;

    @Spy
    private ShopRepository shopRepository;

    @Mock
    private ImageService imageService;

    @Nested
    @DisplayName("생성")
    class create {

        @Test
        @DisplayName("성공")
        void success() {
            // given
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

            shop.setId(1L);

            MultipartFile file = new MockMultipartFile(
                "chicken",
                "chicken.png",
                MediaType.IMAGE_PNG_VALUE,
                "chicken image".getBytes());

            MenuCreateForm menuCreateForm = MenuCreateForm.builder()
                .shopId(1L)
                .name("양념치킨")
                .introduction("맛있는 양념치킨!")
                .price(17000)
                .isSelling(true)
                .file(file)
                .x(0)
                .y(0)
                .length(300)
                .build();

            Menu menu = Menu.builder()
                .shopId(1L)
                .name("양념치킨")
                .introduction("맛있는 양념치킨!")
                .price(17000)
                .isSelling(true)
                .build();

            menu.setId(1L);
            menu.setImagePath("image.jpg");

            given(shopRepository.findById(any()))
                .willReturn(Optional.of(shop));
            given(menuRepository.save(any()))
                .willReturn(menu);

            // when
            menuService.createMenu(menuCreateForm);

            // then
            then(menuRepository)
                .should(times(1))
                .save(any());
            Assertions.assertThat(menu.getMenuOrder()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("조회")
    class Get {

        @Test
        @DisplayName("성공")
        void success() {
            // given
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

            shop.setId(1L);

            Menu menu = Menu.builder()
                .shopId(1L)
                .name("양념치킨")
                .introduction("맛있는 양념치킨!")
                .price(17000)
                .isSelling(true)
                .build();

            menu.setId(1L);
            menu.setImagePath("image.jpg");


            given(shopRepository.findById(any()))
                .willReturn(Optional.of(shop));
            given(menuRepository.findAllByShopIdOrderByOrder(any()))
                .willReturn(List.of(menu));

            // when
            List<MenuResponse> menus = menuService.getMenus(1L);

            // then
            Assertions.assertThat(menus.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("정보 수정")
    class InfoUpdate {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Menu menu = Menu.builder()
                .shopId(1L)
                .name("양념치킨")
                .introduction("맛있는 양념치킨!")
                .price(17000)
                .isSelling(true)
                .build();

            menu.setId(1L);
            menu.setImagePath("image.jpg");

            MultipartFile file = new MockMultipartFile(
                "chicken",
                "chicken.png",
                MediaType.IMAGE_PNG_VALUE,
                "chicken image".getBytes());

            MenuUpdateForm menuUpdateForm = MenuUpdateForm.builder()
                .name("피자")
                .introduction("매콤한 피자!")
                .price(10000)
                .isSelling(true)
                .file(file)
                .x(0)
                .y(0)
                .length(300)
                .build();

            given(menuRepository.findById(any()))
                .willReturn(Optional.of(menu));

            // when
            menuService.update(menuUpdateForm);

            // then
            Assertions.assertThat(menu.getName()).isEqualTo("피자");
            Assertions.assertThat(menu.getIntroduction()).isEqualTo("매콤한 피자!");
            Assertions.assertThat(menu.getPrice()).isEqualTo(10000);
        }
    }

    @Nested
    @DisplayName("순위 수정")
    class MenuOrderUpdate {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Menu menu1 = Menu.builder()
                .shopId(1L)
                .menuCategoryId(1L)
                .name("양념치킨")
                .introduction("맛있는 양념치킨!")
                .price(17000)
                .isSelling(true)
                .build();

            menu1.setId(1L);
            menu1.createMenuOrder();
            menu1.setImagePath("image.jpg");

            Menu menu2 = Menu.builder()
                .shopId(1L)
                .menuCategoryId(1L)
                .name("순살치킨")
                .introduction("맛있는 순살치킨!")
                .price(15000)
                .isSelling(true)
                .build();

            menu2.setId(2L);
            menu2.createMenuOrder();
            menu2.setImagePath("image.jpg");

            Menu menu3 = Menu.builder()
                .shopId(1L)
                .menuCategoryId(1L)
                .name("바삭한치킨")
                .introduction("바삭바삭 치킨!")
                .price(18000)
                .isSelling(true)
                .build();

            menu3.setId(3L);
            menu3.createMenuOrder();
            menu3.setImagePath("image.jpg");

            List<Long> beforeMenuIds = List.of(1L, 2L, 3L);
            List<Long> afterMenuIds = List.of(2L, 3L, 1L);

            MenuOrderUpdateDTO updateDTO = MenuOrderUpdateDTO.builder()
                .menuCategoryId(1L)
                .beforeMenuIds(beforeMenuIds)
                .afterMenuIds(afterMenuIds)
                .build();

            List<Menu> menus = List.of(menu1, menu2, menu3);

            given(menuRepository.findByIdIn(beforeMenuIds))
                .willReturn(menus);

            // when
            menuService.reorderMenus(updateDTO);

            // then
            Assertions.assertThat(menu1.getMenuOrder()).isEqualTo(3);
            Assertions.assertThat(menu2.getMenuOrder()).isEqualTo(1);
            Assertions.assertThat(menu3.getMenuOrder()).isEqualTo(2);

        }
    }
}