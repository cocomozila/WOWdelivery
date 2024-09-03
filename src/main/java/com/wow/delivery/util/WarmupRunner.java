package com.wow.delivery.util;

import com.wow.delivery.controller.OrderController;
import com.wow.delivery.controller.OwnerController;
import com.wow.delivery.controller.ShopController;
import com.wow.delivery.controller.UserController;
import com.wow.delivery.dto.order.OrderCreateDTO;
import com.wow.delivery.dto.owner.OwnerSignupDTO;
import com.wow.delivery.dto.shop.ShopCreateDTO;
import com.wow.delivery.dto.user.UserSignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarmupRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final OwnerController ownerController;
    private final ShopController shopController;
    private final UserController userController;
    private final OrderController orderController;

    private static final int WARM_UP_COUNT = 250;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initOwnerController();
        initUserController();
        initShopController();
        initOrderController();
    }

    private void initOwnerController() {
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                ownerController.signup(new OwnerSignupDTO());
            } catch (Exception e) {}
        }
    }

    private void initUserController() {
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                userController.signup(new UserSignupDTO());
            } catch (Exception e) {}
        }
    }

    private void initShopController() {
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                shopController.getShop(1L);
            } catch (Exception e) {}
        }

        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                shopController.register(new ShopCreateDTO());
            } catch (Exception e) {}
        }
    }

    private void initOrderController() {
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                orderController.register(new OrderCreateDTO());
            } catch (Exception e) {}
        }

        for (int i = 0; i < WARM_UP_COUNT; i++) {
            try {
                orderController.getOrder(1L);
            } catch (Exception e) {}
        }
    }
}
