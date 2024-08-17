package com.wow.delivery.controller;

import com.wow.delivery.dto.user.UserSigninDTO;
import com.wow.delivery.dto.user.UserSigninResponse;
import com.wow.delivery.dto.user.UserSignupDTO;
import com.wow.delivery.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody @Valid UserSignupDTO userSignupDTO) {
        userService.signup(userSignupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserSigninResponse> signin(@RequestBody @Valid UserSigninDTO userSigninDTO, HttpSession session) {
        UserSigninResponse userSigninResponse = userService.signin(userSigninDTO, session);
        return ResponseEntity.ok(userSigninResponse);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        userService.logout(session);
    }
}
