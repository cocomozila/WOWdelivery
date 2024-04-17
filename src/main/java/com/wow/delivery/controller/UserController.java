package com.wow.delivery.controller;

import com.wow.delivery.dto.user.UserSigninDTO;
import com.wow.delivery.dto.user.UserSignupDTO;
import com.wow.delivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody @Valid UserSignupDTO userSignupDTO) {
        userService.signup(userSignupDTO.toEntity());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<HttpStatus> signin(@RequestBody @Valid UserSigninDTO userSigninDTO) {
        userService.signin(userSigninDTO.getEmail(), userSigninDTO.getPassword());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
