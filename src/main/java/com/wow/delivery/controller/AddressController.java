package com.wow.delivery.controller;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressRequestDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid AddressCreateDTO addressCreateDTO) {
        addressService.createAddress(addressCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(@RequestBody AddressRequestDTO addressRequestDTO) {
        return ResponseEntity.ok(addressService.getAddresses(addressRequestDTO));
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateAddress(@RequestBody @Valid AddressUpdateDTO addressUpdateDTO) {
        addressService.updateAddress(addressUpdateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
