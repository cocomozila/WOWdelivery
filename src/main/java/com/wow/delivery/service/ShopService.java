package com.wow.delivery.service;

import com.google.common.geometry.*;
import com.wow.delivery.dto.shop.*;
import com.wow.delivery.entity.Owner;
import com.wow.delivery.entity.shop.*;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final List<String> POPULATED_STREET_NAMES = List.of("세종대로", "테헤란로", "강남대로", "올림픽대로", "도산대로", "서초대로", "봉은사로", "학동로", "언주로", "송파대로", "성수이로", "여의대로", "반포대로", "동작대로", "양재대로");
    private final int S2_REGION_COVERER_MAX_CELLS = 16;
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final MetaCategoryRepository metaCategoryRepository;
    private final OwnerRepository ownerRepository;
    private final OpenDaysRepository openDaysRepository;

    @Transactional
    public Shop createShop(ShopCreateDTO shopCreateDTO) {
        Long ownerId = shopCreateDTO.getOwnerId();
        Owner owner = ownerRepository.findByIdOrThrow(ownerId, ErrorCode.OWNER_DATA_NOT_FOUND, null);

        Shop shop = Shop.builder()
            .owner(owner)
            .shopName(shopCreateDTO.getShopName())
            .introduction(shopCreateDTO.getIntroduction())
            .businessHours(BusinessHours.builder()
                .openTime(shopCreateDTO.getOpenTime())
                .closeTime(shopCreateDTO.getCloseTime())
                .build())
            .minOrderPrice(shopCreateDTO.getMinOrderPrice())
            .state(shopCreateDTO.getState())
            .city(shopCreateDTO.getCity())
            .district(shopCreateDTO.getDistrict())
            .streetName(shopCreateDTO.getStreetName())
            .buildingNumber(shopCreateDTO.getBuildingNumber())
            .addressDetail(shopCreateDTO.getAddressDetail())
            .latitude(shopCreateDTO.getLatitude())
            .longitude(shopCreateDTO.getLongitude())
            .s2LevelToken(new S2LevelToken(shopCreateDTO.getLatitude(), shopCreateDTO.getLongitude()))
            .build();

        shopRepository.save(shop);

        OpenDays openDays = OpenDays.builder()
            .shopId(shop.getId())
            .openDays(shopCreateDTO.getOpenDays())
            .build();

        List<ShopCategory> shopCategories = getShopCategories(shopCreateDTO, shop.getId());

        openDaysRepository.save(openDays);
        shopCategoryRepository.saveAll(shopCategories);
        return shop;
    }

    @Transactional(readOnly = true)
    public List<NearByShopResponse> getCategoryNearByShops(CategoryNearbyShopRequestDTO requestDTO) {
        List<Shop> shops;
        if (isPopulatedArea(requestDTO.getState())) {
            List<String> tokens = getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 2000, 13);
            shops = shopRepository.findByCategoryNearbyShopLevel13(tokens, requestDTO.getSearchCategory());
        } else {
            List<String> tokens = getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 4000, 12);
            shops = shopRepository.findByCategoryNearbyShopLevel12(tokens, requestDTO.getSearchCategory());
        }

        return shops.stream()
            .map(s -> new NearByShopResponse(s.getId(), s.getShopName(), s.getMinOrderPrice()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<NearByShopResponse> getNameNearByShops(NameNearbyShopRequestDTO requestDTO) {
        List<Shop> shops;
        if (isPopulatedArea(requestDTO.getState())) {
            List<String> tokens = getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 2000, 13);
            shops = shopRepository.findByShopNameNearbyShopLevel13(tokens, requestDTO.getSearchShopName());
        } else {
            List<String> tokens = getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 4000, 12);
            shops = shopRepository.findByShopNameNearbyShopLevel12(tokens, requestDTO.getSearchShopName());
        }

        return shops.stream()
            .map(s -> new NearByShopResponse(s.getId(), s.getShopName(), s.getMinOrderPrice()))
            .toList();
    }

    @Transactional
    public void updateShop(ShopUpdateDTO shopUpdateDTO) {
        Shop shop = shopRepository.findByIdOrThrow(shopUpdateDTO.getShopId(), ErrorCode.SHOP_DATA_NOT_FOUND, null);

        shop.update(
            shopUpdateDTO.getShopName(),
            shopUpdateDTO.getIntroduction(),
            BusinessHours.builder()
                .openTime(shopUpdateDTO.getOpenTime())
                .closeTime(shopUpdateDTO.getCloseTime())
                .build(),
            shopUpdateDTO.getMinOrderPrice(),
            shopUpdateDTO.getState(),
            shopUpdateDTO.getCity(),
            shopUpdateDTO.getDistrict(),
            shopUpdateDTO.getStreetName(),
            shopUpdateDTO.getBuildingNumber(),
            shopUpdateDTO.getAddressDetail(),
            shopUpdateDTO.getLatitude(),
            shopUpdateDTO.getLongitude(),
            new S2LevelToken(shopUpdateDTO.getLatitude(), shopUpdateDTO.getLongitude())
        );
    }

    private boolean isPopulatedArea(String state) {
        return POPULATED_STREET_NAMES.contains(state);
    }

    private List<String> getNearbyCellIdTokens(double latitude, double longitude, double radiusMeters, int cellLevel) {
        S2LatLng center = S2LatLng.fromDegrees(latitude, longitude);

        // 반경을 각도로 변환
        double earthCircumferenceMeters = 2 * Math.PI * 6371000; // 지구의 둘레 (미터 단위)
        double angleDegrees = (360.0 * radiusMeters) / earthCircumferenceMeters;
        S1Angle angle = S1Angle.degrees(angleDegrees);

        S2Cap cap = S2Cap.fromAxisAngle(center.toPoint(), angle);

        // 원하는 크기의 셀을 사용하여 covering 수행
        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setMinLevel(cellLevel);
        coverer.setMaxLevel(cellLevel);
        coverer.setMaxCells(S2_REGION_COVERER_MAX_CELLS); // 사용할 셀의 최대 개수를 설정

        ArrayList<S2CellId> coveringCells = new ArrayList<>();
        coverer.getCovering(cap, coveringCells);

        List<String> tokens = new ArrayList<>();
        for (S2CellId cell : coveringCells) {
            tokens.add(cell.toToken());
        }
        return tokens;
    }

    private List<ShopCategory> getShopCategories(ShopCreateDTO createDTO, Long shopId) {
        List<MetaCategory> allByCategoryName = metaCategoryRepository.findAllByCategoryName(createDTO.getCategoryNames());
        return allByCategoryName.stream()
            .map(m -> ShopCategory.builder()
                .shopId(shopId)
                .metaCategoryId(m.getId())
                .build())
            .toList();
    }
}
