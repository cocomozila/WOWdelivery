package com.wow.delivery.service;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.exception.DuplicateEmailException;
import com.wow.delivery.error.exception.DuplicatePhoneNumberException;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDuplicateCheckService {

    private final UserRepository userRepository;

    public void isDuplicateUser(User user) {
        if (isDuplicateEmail(user.getEmail())) {
            throw new DuplicateEmailException("이미 존재하는 이메일 입니다.");
        }
        if (isDuplicatePhoneNumber(user.getPhoneNumber())) {
            throw new DuplicatePhoneNumberException("이미 존재하는 휴대폰 번호 입니다.");
        }
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
