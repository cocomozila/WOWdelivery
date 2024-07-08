package com.wow.delivery.controller;

import com.wow.delivery.dto.rider.RiderSigninDTO;
import com.wow.delivery.dto.rider.RiderSigninResponse;
import com.wow.delivery.dto.rider.RiderSignupDTO;
import com.wow.delivery.service.RiderService;
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
@RequestMapping("/api/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody @Valid RiderSignupDTO riderSignupDTO) {
        riderService.signup(riderSignupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<RiderSigninResponse> signin(@RequestBody @Valid RiderSigninDTO riderSigninDTO, HttpSession session) {
        return ResponseEntity.ok(riderService.signin(riderSigninDTO, session));
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        riderService.logout(session);
    }
}
