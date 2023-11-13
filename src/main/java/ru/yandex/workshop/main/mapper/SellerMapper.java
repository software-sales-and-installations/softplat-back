package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.user.SellerDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.security.dto.UserDto;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
@Component
public interface SellerMapper {

    Seller userDtoToSeller(UserDto userDto);

    @Mapping(target = "imageResponseDto", source = "image")
    SellerResponseDto sellerToSellerResponseDto(Seller seller);

    Seller sellerDtoToSeller(SellerDto sellerDto);

    default BankRequisitesDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesDto(requisites.getId(), requisites.getAccount());
    }

}