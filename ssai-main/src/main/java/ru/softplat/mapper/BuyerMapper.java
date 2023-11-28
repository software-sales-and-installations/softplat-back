package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.dto.user.BuyerUpdateDto;
import ru.softplat.dto.user.response.BuyerResponseDto;
import ru.softplat.dto.user.response.BuyersListResponseDto;
import ru.softplat.model.buyer.Buyer;


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
