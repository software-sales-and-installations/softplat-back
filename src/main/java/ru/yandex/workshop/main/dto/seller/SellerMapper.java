package ru.yandex.workshop.main.dto.seller;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper( SellerMapper.class );

    Seller sellerDtoToSeller(SellerDto sellerDto);

    SellerForResponse sellerToSellerForResponse(Seller seller);

    default BankRequisitesDto requisitesToDto(BankRequisites requisites) {
        return new BankRequisitesDto(requisites.getAccount());
    }

    default BankRequisites requisitesDtoToRequisites(BankRequisitesDto requisites) {
        return new BankRequisites(null, requisites.getAccount());
    }
    //TODO ImageMapper
}