package ru.yandex.workshop.main.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.security.dto.UserDto;

@Mapper
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    @Mapping(target = "email", source = "userDto.email")
    @Mapping(target = "name", source = "userDto.name")
    @Mapping(target = "phone", source = "userDto.phone")
    Buyer buyerDtoToBuyer(UserDto userDto);

    @Mapping(target = "id", source = "buyer.id")
    @Mapping(target = "email", source = "buyer.email")
    @Mapping(target = "name", source = "buyer.name")
    @Mapping(target = "phone", source = "buyer.phone")
    @Mapping(target = "registrationTime", source = "buyer.registrationTime")
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
}
