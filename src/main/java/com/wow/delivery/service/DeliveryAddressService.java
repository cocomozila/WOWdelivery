package com.wow.delivery.service;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressRequestDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.entity.DeliveryAddress;
import com.wow.delivery.entity.User;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.AddressRepository;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createDeliveryAddress(AddressCreateDTO addressCreateDTO) {
        Long userId = addressCreateDTO.getUserId();
        User user = userRepository.findByIdOrThrow(userId, ErrorCode.USER_DATA_NOT_FOUND, null);
        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
            .userId(user.getIdOrThrow())
            .addressAlias(addressCreateDTO.getAddressAlias())
            .address(Address.builder()
                .state(addressCreateDTO.getState())
                .city(addressCreateDTO.getCity())
                .district(addressCreateDTO.getDistrict())
                .streetName(addressCreateDTO.getStreetName())
                .buildingNumber(addressCreateDTO.getBuildingNumber())
                .addressDetail(addressCreateDTO.getAddressDetail())
                .latitude(addressCreateDTO.getLatitude())
                .longitude(addressCreateDTO.getLongitude())
                .build())
            .build();
        addressRepository.save(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(AddressRequestDTO addressRequestDTO) {
        User user = userRepository.findByIdOrThrow(addressRequestDTO.getUserId(), ErrorCode.USER_DATA_NOT_FOUND, null);
        List<DeliveryAddress> deliveryAddresses = addressRepository.findAllByUserId(user.getIdOrThrow());
        return deliveryAddresses.stream()
            .map(deliveryAddress -> new AddressResponse(
                deliveryAddress.getIdOrThrow(),
                deliveryAddress.getAddressAlias(),
                deliveryAddress.getAddress()))
            .toList();
    }

    @Transactional
    public void updateAddress(AddressUpdateDTO addressUpdateDTO) {
        DeliveryAddress deliveryAddress = addressRepository.findByIdOrThrow(addressUpdateDTO.getAddressId(), ErrorCode.ADDRESS_DATA_NOT_FOUND, null);
        deliveryAddress.update(
            addressUpdateDTO.getAddressAlias(),
            Address.builder()
                .state(addressUpdateDTO.getState())
                .city(addressUpdateDTO.getCity())
                .district(addressUpdateDTO.getDistrict())
                .streetName(addressUpdateDTO.getStreetName())
                .buildingNumber(addressUpdateDTO.getBuildingNumber())
                .addressDetail(addressUpdateDTO.getAddressDetail())
                .latitude(addressUpdateDTO.getLatitude())
                .longitude(addressUpdateDTO.getLongitude())
                .build());
    }
}
