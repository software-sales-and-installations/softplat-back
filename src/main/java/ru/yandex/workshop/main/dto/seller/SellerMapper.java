package ru.yandex.workshop.main.dto.seller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;

@Mapper
public interface SellerMapper {

    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    Seller sellerDtoToSeller(SellerDto sellerDto);

    @Mapping(target = "imageResponseDto", expression = "java(mapImageToImageResponseDto(seller))")
    SellerResponseDto sellerToSellerForResponse(Seller seller);

    default BankRequisitesDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesDto(requisites.getId(), requisites.getAccount());
    }

    default ImageResponseDto mapImageToImageResponseDto(Seller seller) {
        Image image = seller.getImage();
        return ImageMapper.INSTANCE.imageToImageResponseDto(image);
    }
}