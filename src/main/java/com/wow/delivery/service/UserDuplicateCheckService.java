package com.wow.delivery.service;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.exception.DuplicateException;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDuplicateCheckService {

    private final UserRepository userRepository;

    public void isDuplicateUser(User user) {
        if (isDuplicateEmail(user.getEmail())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (isDuplicatePhoneNumber(user.getPhoneNumber())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_PHONE_NUMBER);
        }
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
