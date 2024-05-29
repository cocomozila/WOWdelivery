package com.wow.delivery.service;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.entity.Address;
import com.wow.delivery.entity.User;
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
        Long id = addressCreateDTO.getUserId();
        User user = userRepository.getById(id);
        Address address = Address.builder()
            .user(user)
            .addressAlias(addressCreateDTO.getAddressAlias())
            .addressName(addressCreateDTO.getAddressName())
            .detailedAddress(addressCreateDTO.getDetailedAddress())
            .locationX(addressCreateDTO.getLocationX())
            .locationY(addressCreateDTO.getLocationY())
            .build();
        addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(Long userId) {
        User findUser = userRepository.getById(userId);
        List<Address> addresses = addressRepository.findAllByUserId(findUser.getId());
        return addresses.stream()
            .map(address -> new AddressResponse(address.getId(), address.getAddressAlias(), address.getAddressName(),
                address.getDetailedAddress(), address.getLocationX(), address.getLocationY()))
            .toList();
    }

    @Transactional
    public void updateAddress(AddressUpdateDTO addressUpdateDTO) {
        Address address = addressRepository.getById(addressUpdateDTO.getAddressId());
        address.update(addressUpdateDTO.getAddressAlias(), addressUpdateDTO.getAddressName(), addressUpdateDTO.getDetailedAddress(), addressUpdateDTO.getLocationX(), addressUpdateDTO.getLocationY());
    }
}
