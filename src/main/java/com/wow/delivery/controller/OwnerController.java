package com.wow.delivery.controller;

import com.wow.delivery.dto.owner.OwnerSigninDTO;
import com.wow.delivery.dto.owner.OwnerSignupDTO;
import com.wow.delivery.service.OwnerService;
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
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody @Valid OwnerSignupDTO ownerSignupDTO) {
        ownerService.signup(ownerSignupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<HttpStatus> signin(@RequestBody @Valid OwnerSigninDTO ownerSigninDTO, HttpSession session) {
        ownerService.signin(ownerSigninDTO, session);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        ownerService.logout(session);
    }
}
