package com.wow.delivery.repository;

import com.wow.delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        boolean existsByEmail(String email);
        boolean existsByPhoneNumber(String phoneNumber);

        Optional<User> findUserByEmail(String email);
}
