package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.BuyerResponseDto;
import ru.yandex.workshop.security.model.user.Buyer;

@Mapper
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    @Mapping(target = "email", source = "registrationUserDto.email")
    @Mapping(target = "name", source = "registrationUserDto.name")
    @Mapping(target = "password", source = "registrationUserDto.password")
    @Mapping(target = "phone", source = "registrationUserDto.phone")
    @Mapping(target = "role", source = "registrationUserDto.role")
    @Mapping(target = "status", source = "registrationUserDto.status")
    Buyer buyerDtoToBuyer(RegistrationUserDto registrationUserDto);

    @Mapping(target = "id", source = "buyer.id")
    @Mapping(target = "email", source = "buyer.email")
    @Mapping(target = "name", source = "buyer.name")
    @Mapping(target = "phone", source = "buyer.phone")
    @Mapping(target = "registrationTime", source = "buyer.registrationTime")
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
}
