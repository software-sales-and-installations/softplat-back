package ru.yandex.workshop.main.main.dto.buyer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.main.model.buyer.Buyer;

@Mapper
public interface BuyerMapper {
    BuyerMapper INSTANCE = Mappers.getMapper(BuyerMapper.class);

    @Mapping(target = "email", source = "buyerDto.email")
    @Mapping(target = "firstName", source = "buyerDto.firstName")
    @Mapping(target = "lastName", source = "buyerDto.lastName")
    @Mapping(target = "telephone", source = "buyerDto.telephone")
    Buyer buyerDtoToBuyer(BuyerDto buyerDto);

    @Mapping(target = "id", source = "buyer.id")
    @Mapping(target = "email", source = "buyer.email")
    @Mapping(target = "firstName", source = "buyer.firstName")
    @Mapping(target = "lastName", source = "buyer.lastName")
    @Mapping(target = "telephone", source = "buyer.telephone")
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
}
