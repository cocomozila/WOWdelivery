package com.wow.delivery.warmup;

import com.wow.delivery.dto.owner.OwnerSignupDTO;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.BusinessHours;
import com.wow.delivery.entity.shop.S2LevelToken;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.repository.ShopRepository;
import com.wow.delivery.service.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.DayOfWeek;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WarmupRunner implements ApplicationListener<ApplicationReadyEvent> {


    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    private static final int WARM_UP_COUNT = 10;

    private final WebClient webClient;
    private final ShopRepository shopRepository;
    private final OwnerService ownerService;
    private final WarmupTokenProvider warmupTokenProvider;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (ddl.equals("create")) {
            registerDummy();
        }
        initShopController();
    }

    private void initShopController() {
        String warmupToken = warmupTokenProvider.createToken();

        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                webClient.get()
                    .uri("http://localhost:8080/api/shops/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + warmupToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            } catch (Exception e) {
                log.error("Error during warmup GET", e);
            }
        }
    }

    private void registerDummy() {
        createOwnerDummy();
        createShopDummy();
    }

    private void createOwnerDummy() {
        OwnerSignupDTO dto = OwnerSignupDTO.builder()
            .email("dummy@dummy.com")
            .password("dummydummy1")
            .phoneNumber("01012345678")
            .build();

        ownerService.signup(dto);
    }

    private void createShopDummy() {
        ShopEntity shop = ShopEntity.builder()
            .ownerId(1L)
            .shopName("dummy 식당")
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

        shopRepository.save(shop);
    }
}


