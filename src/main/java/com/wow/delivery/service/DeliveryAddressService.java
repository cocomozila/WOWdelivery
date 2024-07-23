package com.wow.delivery.service;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressRequestDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.entity.DeliveryAddressEntity;
import com.wow.delivery.entity.UserEntity;
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
        UserEntity userEntity = userRepository.findByIdOrThrow(userId, ErrorCode.USER_DATA_NOT_FOUND, null);
        DeliveryAddressEntity deliveryAddressEntity = DeliveryAddressEntity.builder()
            .userId(userEntity.getIdOrThrow())
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
        addressRepository.save(deliveryAddressEntity);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(AddressRequestDTO addressRequestDTO) {
        UserEntity userEntity = userRepository.findByIdOrThrow(addressRequestDTO.getUserId(), ErrorCode.USER_DATA_NOT_FOUND, null);
        List<DeliveryAddressEntity> deliveryAddressEntities = addressRepository.findAllByUserId(userEntity.getIdOrThrow());
        return deliveryAddressEntities.stream()
            .map(deliveryAddressEntity -> new AddressResponse(
                deliveryAddressEntity.getIdOrThrow(),
                deliveryAddressEntity.getAddressAlias(),
                deliveryAddressEntity.getAddress()))
            .toList();
    }

    @Transactional
    public void updateAddress(AddressUpdateDTO addressUpdateDTO) {
        DeliveryAddressEntity deliveryAddressEntity = addressRepository.findByIdOrThrow(addressUpdateDTO.getAddressId(), ErrorCode.ADDRESS_DATA_NOT_FOUND, null);
        deliveryAddressEntity.update(
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
