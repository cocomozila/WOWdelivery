package com.wow.delivery.service.shop;

import com.wow.delivery.dto.shop.*;
import com.wow.delivery.entity.OwnerEntity;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.*;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.MetaCategoryRepository;
import com.wow.delivery.repository.OwnerRepository;
import com.wow.delivery.repository.ShopCategoryRepository;
import com.wow.delivery.repository.ShopRepository;
import com.wow.delivery.service.S2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final MetaCategoryRepository metaCategoryRepository;
    private final OwnerRepository ownerRepository;
    private final S2Service s2Service;

    @Transactional
    public void createShop(ShopCreateDTO shopCreateDTO) {
        Long ownerId = shopCreateDTO.getOwnerId();
        OwnerEntity ownerEntity = ownerRepository.findByIdOrThrow(ownerId, ErrorCode.OWNER_DATA_NOT_FOUND, null);

        ShopEntity shopEntity = ShopEntity.builder()
            .ownerId(ownerEntity.getIdOrThrow())
            .shopName(shopCreateDTO.getShopName())
            .introduction(shopCreateDTO.getIntroduction())
            .businessHours(BusinessHours.builder()
                .openTime(shopCreateDTO.getOpenTime())
                .closeTime(shopCreateDTO.getCloseTime())
                .build())
            .minOrderPrice(shopCreateDTO.getMinOrderPrice())
            .address(Address.builder()
                .state(shopCreateDTO.getState())
                .city(shopCreateDTO.getCity())
                .district(shopCreateDTO.getDistrict())
                .streetName(shopCreateDTO.getStreetName())
                .buildingNumber(shopCreateDTO.getBuildingNumber())
                .addressDetail(shopCreateDTO.getAddressDetail())
                .latitude(shopCreateDTO.getLatitude())
                .longitude(shopCreateDTO.getLongitude())
                .build())
            .openDays(shopCreateDTO.getOpenDays())
            .deliveryFee(shopCreateDTO.getDeliveryFee())
            .s2LevelToken(new S2LevelToken(shopCreateDTO.getLatitude(), shopCreateDTO.getLongitude()))
            .build();

        shopCacheService.saveShop(shopEntity);

        List<ShopCategoryEntity> shopCategories = buildShopCategories(shopCreateDTO.getCategoryNames(), shopEntity.getId());
        shopCategoryRepository.saveAll(shopCategories);
    }

    @Transactional(readOnly = true)
    public List<NearbyShopResponse> getShopsByCategory(CategoryNearbyShopRequestDTO requestDTO) {
        return findShopsByDensity(requestDTO,
            shopRepository::findByCategoryNearbyShopLevel13,
            shopRepository::findByCategoryNearbyShopLevel12,
            requestDTO.getSearchCategory()).stream()
            .map(s -> new NearbyShopResponse(s.getId(), s.getShopName(), s.getMinOrderPrice()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<NearbyShopResponse> getShopsByShopName(NameNearbyShopRequestDTO requestDTO) {
        return findShopsByDensity(requestDTO,
            shopRepository::findByShopNameNearbyShopLevel13,
            shopRepository::findByShopNameNearbyShopLevel12,
            requestDTO.getSearchShopName()).stream()
            .map(s -> new NearbyShopResponse(s.getId(), s.getShopName(), s.getMinOrderPrice()))
            .toList();
    }

    private <T> List<ShopEntity> findShopsByDensity(NearbyShopRequestDTO requestDTO,
                                                    BiFunction<List<String>, T, List<ShopEntity>> populatedAreaMethod,
                                                    BiFunction<List<String>, T, List<ShopEntity>> nonPopulatedAreaMethod,
                                                    T searchParameter) {
        if (s2Service.isPopulatedStreetName(requestDTO.getStreetName())) {
            List<String> tokens = s2Service.getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 2000, 13);
            return populatedAreaMethod.apply(tokens, searchParameter);
        }
        List<String> tokens = s2Service.getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 4000, 12);
        return nonPopulatedAreaMethod.apply(tokens, searchParameter);
    }

    @Transactional
    public void updateShop(ShopUpdateDTO shopUpdateDTO) {
        ShopEntity shop = shopCacheService.findByShopIdOrThrow(shopUpdateDTO.getShopId());

        shop.update(
            shopUpdateDTO.getShopName(),
            shopUpdateDTO.getIntroduction(),
            BusinessHours.builder()
                .openTime(shopUpdateDTO.getOpenTime())
                .closeTime(shopUpdateDTO.getCloseTime())
                .build(),
            Address.builder()
                .state(shopUpdateDTO.getState())
                .city(shopUpdateDTO.getCity())
                .district(shopUpdateDTO.getDistrict())
                .streetName(shopUpdateDTO.getStreetName())
                .buildingNumber(shopUpdateDTO.getBuildingNumber())
                .addressDetail(shopUpdateDTO.getAddressDetail())
                .latitude(shopUpdateDTO.getLatitude())
                .longitude(shopUpdateDTO.getLongitude())
                .build(),
            shopUpdateDTO.getOpenDays(),
            shopUpdateDTO.getMinOrderPrice(),
            shopUpdateDTO.getDeliveryFee(),
            new S2LevelToken(shopUpdateDTO.getLatitude(), shopUpdateDTO.getLongitude())
        );
        shopCacheService.saveShop(shop);
    }

    @Transactional(readOnly = true)
    public ShopResponse getShop(Long shopId) {
        ShopEntity shopEntity = shopCacheService.findByShopIdOrThrow(shopId);

        return ShopResponse.builder()
            .shopName(shopEntity.getShopName())
            .introduction(shopEntity.getIntroduction())
            .businessHours(shopEntity.getBusinessHours())
            .address(shopEntity.getAddress())
            .openDays(shopEntity.getOpenDays())
            .minOrderPrice(shopEntity.getMinOrderPrice())
            .build();
    }

    private List<ShopCategoryEntity> buildShopCategories(List<String> categoryNames, Long shopId) {
        List<MetaCategoryEntity> allByCategoryName = metaCategoryRepository.findAllByCategoryNameIn(categoryNames);
        return allByCategoryName.stream()
            .map(m -> ShopCategoryEntity.builder()
                .shopId(shopId)
                .metaCategoryId(m.getId())
                .build())
            .toList();
    }
}
