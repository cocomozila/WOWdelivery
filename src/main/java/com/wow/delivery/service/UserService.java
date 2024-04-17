package com.wow.delivery.service;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.exception.InvalidCredentialsException;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.util.PasswordEncoder;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDuplicateCheckService userDuplicateCheckService;

    public void signup(User user) {
        userDuplicateCheckService.isDuplicateUser(user);
        userRepository.save(user);
    }

    public void signin(String email, String password) {
        User findUser = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new InvalidCredentialsException("이메일이나 비밀번호가 일치하지 않습니다."));

        if (!PasswordEncoder.matchesPassword(password, findUser.getPassword(), findUser.getSalt())) {
            throw new InvalidCredentialsException("이메일이나 비밀번호가 일치하지 않습니다.");
        }
    }
}
