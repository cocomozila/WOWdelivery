package com.wow.delivery.service;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.util.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
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
    public void signin(String email, String password, HttpSession session) {
        User findUser = userRepository.getByEmail(email);

        if (!PasswordEncoder.matchesPassword(password, findUser.getPassword(), findUser.getSalt())) {
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 계정을 찾을 수 없습니다.");
        }
        setSession(findUser, session);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    private void setSession(User user, HttpSession session) {
        session.setAttribute("userEmail", user.getEmail());
        session.setMaxInactiveInterval(60 * 30);
    }

    private void validDuplicateUser(User user) {
        if (isDuplicateEmail(user.getEmail())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 이메일 입니다.");
        }
        if (isDuplicatePhoneNumber(user.getPhoneNumber())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 휴대폰 번호 입니다.");
        }
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
