package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.dto.user.BuyerUpdateDto;
import ru.softplat.dto.user.response.BuyerResponseDto;
import ru.softplat.dto.user.response.BuyersListResponseDto;


import java.util.List;

@Mapper
@Component
public interface BuyerMapper {
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);

    Buyer buyerDtoToBuyer(BuyerUpdateDto buyerUpdateDto);

    default BuyersListResponseDto toBuyersListResponseDto(List<BuyerResponseDto> buyers) {
        return BuyersListResponseDto.builder().buyers(buyers).build();
    }
}
