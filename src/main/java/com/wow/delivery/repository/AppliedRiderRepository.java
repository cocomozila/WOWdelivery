package com.wow.delivery.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppliedRiderRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long addRider(Long orderId) {
        return redisTemplate.opsForSet()
            .add(orderId.toString(), "assingRider");
    }

    public Long removeRider(Long orderId) {
        return redisTemplate.opsForSet()
            .remove(orderId.toString(), "assingRider");
    }
}
