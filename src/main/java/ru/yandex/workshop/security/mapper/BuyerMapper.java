package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.security.dto.response.BuyerResponseDto;
import ru.yandex.workshop.security.model.Buyer;

@Mapper
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    @Mapping(target = "id", source = "buyer.id")
    @Mapping(target = "email", source = "buyer.email")
    @Mapping(target = "name", source = "buyer.name")
    @Mapping(target = "telephone", source = "buyer.telephone")
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
}
