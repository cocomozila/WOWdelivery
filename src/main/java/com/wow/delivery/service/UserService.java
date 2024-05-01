package com.wow.delivery.service;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(User user) {
        validDuplicateUser(user);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void signin(String email, String password) {
        User findUser = userRepository.getUserByEmail(email);

        if (!PasswordEncoder.matchesPassword(password, findUser.getPassword(), findUser.getSalt())) {
            throw new DataNotFoundException(ErrorCode.MISMATCH_ACCOUNT);
        }
    }

    private void validDuplicateUser(User user) {
        if (isDuplicateEmail(user.getEmail())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (isDuplicatePhoneNumber(user.getPhoneNumber())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_PHONE_NUMBER);
        }
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
