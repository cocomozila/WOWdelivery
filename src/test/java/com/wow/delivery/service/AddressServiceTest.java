package com.wow.delivery.service;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressRequestDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.entity.Address;
import com.wow.delivery.entity.User;
import com.wow.delivery.repository.AddressRepository;
import com.wow.delivery.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Spy
    private AddressRepository addressRepository;

    @Spy
    private UserRepository userRepository;

    @Nested
    @DisplayName("주소 등록")
    class CreateAddress {

        @Test
        @DisplayName("성공")
        void createAddress() {
            // given
            AddressCreateDTO addressCreateDTO = AddressCreateDTO.builder()
                .userId(1L)
                .addressAlias("우리집")
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("호국로")
                .buildingNumber("754")
                .addressDetail("000동 000호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            User user = User.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            addressService.createAddress(addressCreateDTO);

            // then
            then(addressRepository)
                .should(times(1))
                .save(any());
        }
    }

    @Nested
    @DisplayName("주소 조회")
    class FindAddress {

        @Test
        @DisplayName("성공")
        void success_findAddress() {
            // given
            User user = User.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            Long userId = 1L;

            Address address1 = Address.builder()
                .user(user)
                .addressAlias("우리집")
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("호국로")
                .buildingNumber("754")
                .addressDetail("000동 000호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            Address address2 = Address.builder()
                .user(user)
                .addressAlias("사무실")
                .state("서울특별시")
                .city("서울시")
                .district("강남구")
                .streetName("강남대로")
                .buildingNumber("31")
                .addressDetail("무슨 오피스텔 401호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .userId(userId)
                .build();

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(addressRepository.findAllByUserId(any())).willReturn(List.of(address1, address2));

            // when
            List<AddressResponse> addresses = addressService.getAddresses(addressRequestDTO);

            // then
            Assertions.assertThat(addresses).hasSize(2);
        }

        @Test
        @DisplayName("주소 0개 성공")
        void success_findAddress_zero() {
            // given
            User user = User.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            Long userId = 1L;

            AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .userId(userId)
                .build();

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(addressRepository.findAllByUserId(any())).willReturn(List.of());

            // when
            List<AddressResponse> addresses = addressService.getAddresses(addressRequestDTO);

            // then
            Assertions.assertThat(addresses).hasSize(0);
        }
    }

    @Nested
    @DisplayName("주소 변경")
    class UpdateAddress {

        @Test
        @DisplayName("성공")
        void updateAddress() {
            // given
            User user = User.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            Address address1 = Address.builder()
                .user(user)
                .addressAlias("우리집")
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("호국로")
                .buildingNumber("754")
                .addressDetail("000동 000호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            AddressUpdateDTO addressUpdateDTO = AddressUpdateDTO.builder()
                .addressId(1L)
                .addressAlias("변경된 집")
                .state("경기도")
                .city("고양시")
                .district("덕양구")
                .streetName("호국로")
                .buildingNumber("742")
                .addressDetail("111동 111호")
                .latitude(126.8338819)
                .longitude(37.6639380)
                .build();

            given(addressRepository.findById(anyLong())).willReturn(Optional.of(address1));

            // when
            addressService.updateAddress(addressUpdateDTO);

            // then
            Assertions.assertThat(address1.getAddressAlias()).isEqualTo("변경된 집");
            Assertions.assertThat(address1.getAddressDetail()).isEqualTo("111동 111호");
            Assertions.assertThat(address1.getLatitude()).isEqualTo(126.8338819);
            Assertions.assertThat(address1.getLongitude()).isEqualTo(37.6639380);
        }
    }
}