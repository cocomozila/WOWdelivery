package com.wow.delivery.service;

import com.wow.delivery.dto.address.AddressCreateDTO;
import com.wow.delivery.dto.address.AddressRequestDTO;
import com.wow.delivery.dto.address.AddressResponse;
import com.wow.delivery.dto.address.AddressUpdateDTO;
import com.wow.delivery.entity.DeliveryAddressEntity;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.entity.common.Address;
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
class DeliveryAddressServiceTest {

    @InjectMocks
    private DeliveryAddressService deliveryAddressService;

    @Spy
    private AddressRepository addressRepository;

    @Spy
    private UserRepository userRepository;

    @Nested
    @DisplayName("주소 등록")
    class CreateDeliveryAddressEntity {

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

            UserEntity userEntity = UserEntity.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            userEntity.setId(1L);

            given(userRepository.findById(anyLong())).willReturn(Optional.of(userEntity));

            // when
            deliveryAddressService.createDeliveryAddress(addressCreateDTO);

            // then
            then(addressRepository)
                .should(times(1))
                .save(any());
        }
    }

    @Nested
    @DisplayName("주소 조회")
    class FindDeliveryAddressEntity {

        @Test
        @DisplayName("성공")
        void success_findAddress() {
            // given
            UserEntity userEntity = UserEntity.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            userEntity.setId(1L);

            DeliveryAddressEntity deliveryAddressEntity1 = DeliveryAddressEntity.builder()
                .userId(1L)
                .addressAlias("우리집")
                .address(Address.builder()
                    .state("경기도")
                    .city("고양시")
                    .district("덕양구")
                    .streetName("호국로")
                    .buildingNumber("754")
                    .addressDetail("000동 000호")
                    .latitude(126.8338819)
                    .longitude(37.6639380)
                    .build())
                .build();

            deliveryAddressEntity1.setId(1L);

            DeliveryAddressEntity deliveryAddressEntity2 = DeliveryAddressEntity.builder()
                .userId(1L)
                .addressAlias("사무실")
                .address(Address.builder()
                    .state("서울특별시")
                    .city("서울시")
                    .district("강남구")
                    .streetName("강남대로")
                    .buildingNumber("31")
                    .addressDetail("무슨 오피스텔 401호")
                    .latitude(126.8338819)
                    .longitude(37.6639380)
                    .build())
                .build();

            deliveryAddressEntity2.setId(2L);

            AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .userId(userEntity.getIdOrThrow())
                .build();

            given(userRepository.findById(userEntity.getIdOrThrow())).willReturn(Optional.of(userEntity));
            given(addressRepository.findAllByUserId(any())).willReturn(List.of(deliveryAddressEntity1, deliveryAddressEntity2));

            // when
            List<AddressResponse> addresses = deliveryAddressService.getAddresses(addressRequestDTO);

            // then
            Assertions.assertThat(addresses).hasSize(2);
        }

        @Test
        @DisplayName("주소 0개 성공")
        void success_findAddress_zero() {
            // given
            UserEntity userEntity = UserEntity.builder()
                .email("abcd@gmail.com")
                .password("12345678")
                .salt("abcd1234")
                .phoneNumber("01011112222")
                .build();

            userEntity.setId(1L);

            AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .userId(userEntity.getIdOrThrow())
                .build();

            given(userRepository.findById(userEntity.getIdOrThrow())).willReturn(Optional.of(userEntity));
            given(addressRepository.findAllByUserId(any())).willReturn(List.of());

            // when
            List<AddressResponse> addresses = deliveryAddressService.getAddresses(addressRequestDTO);

            // then
            Assertions.assertThat(addresses).hasSize(0);
        }
    }

    @Nested
    @DisplayName("주소 변경")
    class UpdateDeliveryAddressEntity {

        @Test
        @DisplayName("성공")
        void updateAddress() {
            // given
            DeliveryAddressEntity deliveryAddressEntity1 = DeliveryAddressEntity.builder()
                .userId(1L)
                .addressAlias("우리집")
                .address(Address.builder()
                    .state("경기도")
                    .city("고양시")
                    .district("덕양구")
                    .streetName("호국로")
                    .buildingNumber("754")
                    .addressDetail("000동 000호")
                    .latitude(126.8338819)
                    .longitude(37.6639380)
                    .build())
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

            given(addressRepository.findById(anyLong())).willReturn(Optional.of(deliveryAddressEntity1));

            // when
            deliveryAddressService.updateAddress(addressUpdateDTO);

            // then
            Assertions.assertThat(deliveryAddressEntity1.getAddressAlias()).isEqualTo("변경된 집");
            Assertions.assertThat(deliveryAddressEntity1.getAddress().getAddressDetail()).isEqualTo("111동 111호");
            Assertions.assertThat(deliveryAddressEntity1.getAddress().getLatitude()).isEqualTo(126.8338819);
            Assertions.assertThat(deliveryAddressEntity1.getAddress().getLongitude()).isEqualTo(37.6639380);
        }
    }
}