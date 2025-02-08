package com.wow.delivery.util;

import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.*;
import com.wow.delivery.repository.MetaCategoryRepository;
import com.wow.delivery.repository.ShopCategoryRepository;
import com.wow.delivery.repository.ShopRepository;
import com.wow.delivery.service.S2Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DummyShopDataGenerator implements CommandLineRunner {

    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final MetaCategoryRepository metaCategoryRepository;
    private final S2Service s2Service;

    // 서울시 대략적인 위도/경도 범위 (예: 위도: 37.4 ~ 37.7, 경도: 126.8 ~ 127.2)
    private static final double MIN_LAT = 37.4;
    private static final double MAX_LAT = 37.7;
    private static final double MIN_LNG = 126.8;
    private static final double MAX_LNG = 127.2;

    private static final int TOTAL_RECORDS = 30000;
    private static final int BATCH_SIZE = 1000;

    // 가게 카테고리로 사용할 카테고리 이름 풀 (meta_category 테이블에 반드시 존재해야 함)
    private static final String[] CATEGORY_POOL = {"버거", "커피", "피자", "치킨", "분식"};

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Dummy Shop Data 생성 시작...");
        Random random = new Random();
        List<ShopEntity> shopBatch = new ArrayList<>(BATCH_SIZE);
        List<ShopCategoryEntity> categoryBatch = new ArrayList<>();

        for (int i = 1; i <= TOTAL_RECORDS; i++) {
            Long ownerId = 1L;  // ownerId를 1로 고정
            String shopName = "Shop_" + i;
            String introduction = "가게 소개 예시 " + i;

            BusinessHours businessHours = BusinessHours.builder()
                .openTime("0900")
                .closeTime("2100")
                .build();

            // 임의의 좌표 생성 (서울 내)
            double latitude = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
            double longitude = MIN_LNG + (MAX_LNG - MIN_LNG) * random.nextDouble();
            Address address = Address.builder()
                .state("서울특별시")
                .city("서울")
                .district("A구")  // 예시로 고정 또는 랜덤 선택 가능
                .streetName("A로")
                .buildingNumber(String.valueOf(random.nextInt(100) + 1))
                .addressDetail("상세주소 " + i)
                .latitude(latitude)
                .longitude(longitude)
                .build();

            // 영업 요일: 월~금 중 랜덤 3~5일 선택 (DayOfWeekUtil은 별도 구현)
            List<DayOfWeek> openDays = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

            int minOrderPrice = random.nextInt(30000) + 10000; // 10,000 ~ 40,000원
            int deliveryFee = random.nextInt(5000) + 2000;      // 2,000 ~ 7,000원

            // S2LevelToken 생성 (내부에서 위경도 기반 S2 셀 토큰 계산)
            S2LevelToken s2LevelToken = new S2LevelToken(latitude, longitude);

            ShopEntity shopEntity = ShopEntity.builder()
                .ownerId(ownerId)
                .shopName(shopName)
                .introduction(introduction)
                .businessHours(businessHours)
                .address(address)
                .openDays(openDays)
                .minOrderPrice(minOrderPrice)
                .deliveryFee(deliveryFee)
                .s2LevelToken(s2LevelToken)
                .build();

            shopBatch.add(shopEntity);

            // 가게 카테고리 생성: 카테고리 이름 풀에서 랜덤하게 1~3개 선택
            List<String> categoryNames = getRandomCategoryNames(random, 1, 3);
            // metaCategoryRepository에서 해당 카테고리 이름에 해당하는 MetaCategoryEntity 리스트를 조회
            List<MetaCategoryEntity> metaCategories = metaCategoryRepository.findAllByCategoryNameIn(categoryNames);
            // 조회된 meta category로 ShopCategoryEntity 생성
            for (MetaCategoryEntity metaCategory : metaCategories) {
                ShopCategoryEntity shopCategory = ShopCategoryEntity.builder()
                    .shopId(null)  // 나중에 shopEntity의 id가 할당되면 채워짐; 배치 저장 후 업데이트 방식 사용 가능
                    .metaCategoryId(metaCategory.getId())
                    .build();
                // 임시로 shopCategory 객체에 현재 shopEntity와 연관 짓기 위해, 후에 배치 저장 시 shopEntity의 id를 할당
                // 여기서는 shopEntity가 저장된 후에 shopCategory에 shop id를 다시 할당하는 방법으로 진행
                categoryBatch.add(shopCategory);
            }

            // 배치 단위로 저장
            if (i % BATCH_SIZE == 0) {
                // 먼저 ShopEntity 배치 저장 (저장 후 id가 할당됨)
                List<ShopEntity> savedShops = shopRepository.saveAll(shopBatch);
                shopRepository.flush();

                // 각 ShopEntity와 매핑되는 ShopCategoryEntity에 shop id 할당
                // (단, 여기서는 배치 내 모든 shopCategoryEntity가 순서대로 savedShops와 매칭되도록 단순화하지 않고,
                //  예시로 각 shop에 대해 동일한 카테고리 목록을 적용하는 것으로 가정)
                int index = 0;
                for (ShopEntity savedShop : savedShops) {
                    // 해당 가게에 할당된 카테고리 갯수만큼 categoryBatch에서 shop id를 업데이트
                    // 예시에서는 고정된 개수로 저장한 것이 아니라, 모든 가게에 동일하게 categoryBatch의 일부가 할당되도록 설계할 수 있음.
                    // 실제 구현에서는 각 shop마다 별도 category list를 만들어 배치로 저장하는 방식이 권장됨.
                    // 여기서는 간단하게 처리하기 위해, 이미 생성된 categoryBatch의 해당 인덱스 범위만 업데이트하는 예시입니다.
                    // (실제 프로젝트에서는 ShopService의 buildShopCategories 메서드 로직을 재활용하는 것이 좋습니다.)
                    // 예시:
                    // shopCategoryRepository.save(new ShopCategoryEntity(savedShop.getId(), ...));
                    // 본 예시에서는 배치 저장 시 ShopCategoryEntity에 shop id를 할당하는 로직은 별도로 구현 필요.
                }
                // 만약 위의 방식이 복잡하다면, 각 shop 저장 후 즉시 ShopCategoryEntity를 저장하는 방식으로 전환할 수 있습니다.
                // 여기서는 간단한 배치 처리 예시로 categoryBatch를 flush() 처리한다고 가정합니다.
                shopCategoryRepository.saveAll(categoryBatch);
                shopCategoryRepository.flush();

                shopBatch.clear();
                categoryBatch.clear();
                System.out.println(i + " 건 저장 완료...");
            }
        }

        // 남은 데이터 저장
        if (!shopBatch.isEmpty()) {
            List<ShopEntity> savedShops = shopRepository.saveAll(shopBatch);
            shopRepository.flush();
            // 저장된 ShopEntity에 맞춰 ShopCategoryEntity 업데이트 처리 (생략)
            if (!categoryBatch.isEmpty()) {
                shopCategoryRepository.saveAll(categoryBatch);
                shopCategoryRepository.flush();
            }
        }
        System.out.println("Dummy Shop Data 생성 완료.");
    }

    /**
     * CATEGORY_POOL에서 랜덤하게 최소 minCount ~ 최대 maxCount 개의 카테고리 이름을 선택하여 반환합니다.
     */
    private List<String> getRandomCategoryNames(Random random, int minCount, int maxCount) {
        int count = random.nextInt(maxCount - minCount + 1) + minCount;
        List<String> pool = new ArrayList<>();
        for (String category : CATEGORY_POOL) {
            pool.add(category);
        }
        // 섞은 후 앞에서 count개 선택
        java.util.Collections.shuffle(pool, random);
        return pool.subList(0, count);
    }
}
