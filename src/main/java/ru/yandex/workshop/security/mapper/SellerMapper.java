package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.dto.user.SellerDto;
import ru.yandex.workshop.security.model.Seller;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    Seller sellerDtoToSeller(SellerDto sellerDto);

    SellerResponseDto sellerToSellerForResponse(Seller seller);

    default BankRequisitesDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesDto(requisites.getAccount());
    }

    default BankRequisites requisitesDtoToRequisites(BankRequisitesDto requisites) {
        if (requisites == null) return null;
        return new BankRequisites(null, requisites.getAccount());
    }
    //TODO ImageMapper
}