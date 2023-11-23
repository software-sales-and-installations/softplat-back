package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.seller.BankRequisitesResponseDto;
import ru.yandex.workshop.main.dto.user.SellerUpdateDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.dto.user.response.SellersListResponseDto;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;

import java.util.List;

@Mapper(uses = ImageMapper.class)
@Component
public interface SellerMapper {
    @Mapping(target = "imageResponseDto", source = "image")
    SellerResponseDto sellerToSellerResponseDto(Seller seller);

    Seller sellerDtoToSeller(SellerUpdateDto sellerUpdateDto);

    default BankRequisitesResponseDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesResponseDto(requisites.getId(), requisites.getAccount());
    }

    default SellersListResponseDto toSellersListResponseDto(List<SellerResponseDto> sellers) {
        return SellersListResponseDto.builder().sellers(sellers).build();
    }
}