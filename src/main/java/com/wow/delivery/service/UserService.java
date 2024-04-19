package com.wow.delivery.service;

import com.wow.delivery.entity.User;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.MismatchException;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.util.PasswordEncoder;
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
                .orElseThrow(()-> new MismatchException(ErrorCode.MISMATCH_ACCOUNT));


        if (!PasswordEncoder.matchesPassword(password, findUser.getPassword(), findUser.getSalt())) {
            throw new MismatchException(ErrorCode.MISMATCH_ACCOUNT);
        }
    }
}
