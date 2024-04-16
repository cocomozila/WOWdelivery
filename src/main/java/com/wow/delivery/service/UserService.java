package com.wow.delivery.service;

import com.wow.delivery.entity.User;
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

}
