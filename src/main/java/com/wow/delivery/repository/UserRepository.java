package com.wow.delivery.repository;

import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;

import java.util.Optional;

public interface UserRepository extends CustomJpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findUserByEmail(String email);

    default UserEntity getByEmail(String email) {
        return findUserByEmail(email)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 계정을 찾을 수 없습니다."));
    }
}
