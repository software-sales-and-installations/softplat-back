package ru.yandex.workshop.main.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.security.dto.UserDto;

@Mapper
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    Buyer buyerDtoToBuyer(UserDto userDto);

    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
}
