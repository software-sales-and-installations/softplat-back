package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.model.buyer.Buyer;

@Mapper
@Component
public interface BuyerMapper {
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);

    Buyer buyerDtoToBuyer(BuyerDto buyerDto);
}
