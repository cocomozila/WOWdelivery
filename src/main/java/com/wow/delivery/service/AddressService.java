package com.wow.delivery.service;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressRequestDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.entity.Address;
import com.wow.delivery.entity.User;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.AddressRepository;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createAddress(AddressCreateDTO addressCreateDTO) {
        Long userId = addressCreateDTO.getUserId();
        User user = userRepository.findByIdOrThrow(userId, ErrorCode.USER_DATA_NOT_FOUND, null);
        Address address = Address.builder()
            .user(user)
            .addressAlias(addressCreateDTO.getAddressAlias())
            .state(addressCreateDTO.getState())
            .city(addressCreateDTO.getCity())
            .district(addressCreateDTO.getDistrict())
            .streetName(addressCreateDTO.getStreetName())
            .buildingNumber(addressCreateDTO.getBuildingNumber())
            .addressDetail(addressCreateDTO.getAddressDetail())
            .latitude(addressCreateDTO.getLatitude())
            .longitude(addressCreateDTO.getLongitude())
            .build();
        addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(AddressRequestDTO addressRequestDTO) {
        User user = userRepository.findByIdOrThrow(addressRequestDTO.getUserId(), ErrorCode.USER_DATA_NOT_FOUND, null);
        List<Address> addresses = addressRepository.findAllByUserId(user.getId());
        return addresses.stream()
            .map(address -> new AddressResponse(address.getId(), address.getAddressAlias(), address.getState(),
                address.getCity(), address.getDistrict(), address.getStreetName(), address.getBuildingNumber(),
                address.getAddressDetail(), address.getLatitude(), address.getLongitude()))
            .toList();
    }

    @Transactional
    public void updateAddress(AddressUpdateDTO addressUpdateDTO) {
        Address address = addressRepository.findByIdOrThrow(addressUpdateDTO.getAddressId(), ErrorCode.ADDRESS_DATA_NOT_FOUND, null);
        address.update(addressUpdateDTO.getAddressAlias(), addressUpdateDTO.getState(),
            addressUpdateDTO.getCity(), addressUpdateDTO.getDistrict(), addressUpdateDTO.getStreetName(), addressUpdateDTO.getBuildingNumber(),
            addressUpdateDTO.getAddressDetail(), addressUpdateDTO.getLatitude(), addressUpdateDTO.getLongitude());
    }
}
