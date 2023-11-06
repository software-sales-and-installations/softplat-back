package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.dto.user.SellerDto;
import ru.yandex.workshop.security.model.user.Seller;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    Seller regSellerDtoToSeller(RegistrationUserDto registrationUserDto);

    @Mapping(target = "imageResponseDto", expression = "java(mapImageToImageResponseDto(seller))")
    SellerResponseDto sellerToSellerResponseDto(Seller seller);

    Seller sellerDtoToSeller(SellerDto sellerDto);

    default BankRequisitesDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesDto(requisites.getId(), requisites.getAccount());
    }

    default ImageResponseDto mapImageToImageResponseDto(Seller seller) {
        Image image = seller.getImage();
        return ImageMapper.INSTANCE.imageToImageResponseDto(image);
    }
}