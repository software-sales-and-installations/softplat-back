package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.model.user.Seller;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    @Mapping(target = "email", source = "registrationUserDto.email")
    @Mapping(target = "name", source = "registrationUserDto.name")
    @Mapping(target = "password", source = "registrationUserDto.password")
    @Mapping(target = "phone", source = "registrationUserDto.phone")
    @Mapping(target = "role", source = "registrationUserDto.role")
    @Mapping(target = "status", source = "registrationUserDto.status")
    Seller sellerDtoToSeller(RegistrationUserDto registrationUserDto);

    @Mapping(target = "id", source = "seller.id")
    @Mapping(target = "email", source = "seller.email")
    @Mapping(target = "name", source = "seller.name")
    @Mapping(target = "phone", source = "seller.phone")
    @Mapping(target = "description", source = "seller.description")
    @Mapping(target = "registrationTime", source = "seller.registrationTime")
    @Mapping(target = "requisites", source = "seller.requisites")
    SellerResponseDto sellerToSellerResponseDto(Seller seller);

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