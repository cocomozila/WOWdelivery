package com.wow.delivery.controller;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/address/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid AddressCreateDTO addressCreateDTO) {
        addressService.createAddress(addressCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getAddresses(@RequestBody Map<String, Long> id) {
        return ResponseEntity.ok(addressService.getAddresses(id.get("userId")));
    }

    @PutMapping("/address")
    public void updateAddress(@RequestBody @Valid AddressUpdateDTO addressUpdateDTO) {
        addressService.updateAddress(addressUpdateDTO);
    }
}
