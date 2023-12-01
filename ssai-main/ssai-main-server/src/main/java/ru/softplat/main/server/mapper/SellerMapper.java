package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.dto.user.SellerUpdateDto;
import ru.softplat.main.dto.user.response.SellerResponseDto;
import ru.softplat.main.dto.user.response.SellersListResponseDto;
import ru.softplat.main.server.model.seller.BankRequisites;
import ru.softplat.main.server.model.seller.Seller;
import ru.softplat.security.dto.UserCreateMainDto;

import java.util.List;

@Mapper(uses = ImageMapper.class)
@Component
public interface SellerMapper {
    @Mapping(target = "imageResponseDto", source = "image")
    SellerResponseDto sellerToSellerResponseDto(Seller seller);

    Seller sellerDtoToSeller(SellerUpdateDto sellerUpdateDto);

    Seller userCreateDtoToSeller(UserCreateMainDto userCreateMainDto);

    default BankRequisitesResponseDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesResponseDto(requisites.getId(), requisites.getAccount());
    }

    default SellersListResponseDto toSellersListResponseDto(List<SellerResponseDto> sellers) {
        return SellersListResponseDto.builder().sellers(sellers).build();
    }
}