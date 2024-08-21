package com.wow.delivery.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppliedRiderRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long addRider(Long orderId, Long riderId) {
        return redisTemplate.opsForSet()
            .add(orderId.toString(), riderId.toString());
    }
}
