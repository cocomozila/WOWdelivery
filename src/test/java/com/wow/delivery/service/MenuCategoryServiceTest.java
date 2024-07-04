package com.wow.delivery.service;

import com.wow.delivery.dto.menu.category.MenuCategoryCreateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryOrderUpdateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryResponse;
import com.wow.delivery.entity.menu.MenuCategory;
import com.wow.delivery.entity.shop.Shop;
import com.wow.delivery.repository.MenuCategoryRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuCategoryServiceTest {

    @InjectMocks
    private MenuCategoryService menuCategoryService;

    @Spy
    private ShopRepository shopRepository;

    @Spy
    private MenuCategoryRepository menuCategoryRepository;

    @Mock
    private ShopService shopService;

    @Nested
    @DisplayName("생성")
    class Create {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Shop shop = Shop.builder()
                .build();
            shop.setId(1L);

            MenuCategoryCreateDTO createDTO = MenuCategoryCreateDTO.builder()
                .shopId(1L)
                .name("치킨")
                .build();

            MenuCategory menuCategory = MenuCategory.builder()
                .shopId(1L)
                .name("치킨")
                .build();

            menuCategory.setId(1L);
            menuCategory.createCategoryOrder();

            given(shopService.findByShopIdOrThrow(1L))
                .willReturn(shop);
            given(menuCategoryRepository.save(any()))
                .willReturn(menuCategory);

            // when
            menuCategoryService.createMenuCategory(createDTO);

            // then
            then(menuCategoryRepository)
                .should(times(1)).save(any());

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
                .build();
            shop.setId(1L);

            MenuCategory menuCategory1 = MenuCategory.builder()
                .shopId(1L)
                .name("치킨")
                .build();
            menuCategory1.setId(1L);

            MenuCategory menuCategory2 = MenuCategory.builder()
                .shopId(1L)
                .name("치킨")
                .build();
            menuCategory2.setId(2L);

            given(shopService.findByShopIdOrThrow(1L))
                .willReturn(shop);
            given(menuCategoryRepository.findAllByShopId(1L))
                .willReturn(Optional.of(List.of(menuCategory1, menuCategory2)));

            // when
            List<MenuCategoryResponse> menuCategories = menuCategoryService.getMenuCategory(1L);

            // then
            Assertions.assertThat(menuCategories.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("수정")
    class Update {

        @Test
        @DisplayName("순서 변경 성공")
        void successReorder() {
            // given
            Shop shop = Shop.builder()
                .build();
            shop.setId(1L);

            MenuCategory menuCategory1 = MenuCategory.builder()
                .shopId(1L)
                .name("치킨")
                .build();
            menuCategory1.setId(1L);
            menuCategory1.createCategoryOrder();

            MenuCategory menuCategory2 = MenuCategory.builder()
                .shopId(1L)
                .name("치킨")
                .build();
            menuCategory2.setId(2L);
            menuCategory2.createCategoryOrder();

            MenuCategory menuCategory3 = MenuCategory.builder()
                .shopId(1L)
                .name("치킨")
                .build();
            menuCategory3.setId(3L);
            menuCategory3.createCategoryOrder();

            List<Long> beforeIds = List.of(1L, 2L, 3L);
            List<Long> afterIds = List.of(2L, 3L, 1L);

            MenuCategoryOrderUpdateDTO updateDTO = MenuCategoryOrderUpdateDTO.builder()
                .shopId(1L)
                .beforeIds(beforeIds)
                .afterIds(afterIds)
                .build();

            List<MenuCategory> menuCategories = List.of(menuCategory3, menuCategory2, menuCategory1);

            given(menuCategoryRepository.findByIdIn(beforeIds))
                .willReturn(menuCategories);

            // when
            menuCategoryService.reorderMenuCategories(updateDTO);

            // then
            Assertions.assertThat(menuCategory1.getMenuCategoryOrder()).isEqualTo(3);
            Assertions.assertThat(menuCategory2.getMenuCategoryOrder()).isEqualTo(1);
            Assertions.assertThat(menuCategory3.getMenuCategoryOrder()).isEqualTo(2);
        }
    }
}