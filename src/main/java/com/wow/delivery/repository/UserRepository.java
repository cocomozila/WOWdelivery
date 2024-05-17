package com.wow.delivery.repository;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;

public interface UserRepository extends CustomJpaRepository<User, Long> {

public interface UserRepository extends CustomJpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findUserByEmail(String email);

    default User getByEmail(String email) {
        return findUserByEmail(email)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 계정을 찾을 수 없습니다."));
    }
}
