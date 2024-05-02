package com.wow.delivery.repository;

import com.wow.delivery.entity.Owner;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Owner> findUserByEmail(String email);

    default Owner getOwnerByEmail(String email) {
        return findUserByEmail(email)
            .orElseThrow(()-> new DataNotFoundException(ErrorCode.MISMATCH_ACCOUNT));
    }
}
