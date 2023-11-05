package ru.yandex.workshop.main.dto.buyer;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.model.buyer.Buyer;

@Mapper
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    Buyer buyerDtoToBuyer(BuyerDto buyerDto);

    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
}
