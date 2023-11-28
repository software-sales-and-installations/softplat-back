package ru.softplat.dto.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.softplat.dto.image.ImageResponseDto;
import ru.softplat.model.seller.BankRequisites;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerResponseDto {
    Long id;
    String email;
    String name;
    String phone;
    LocalDateTime registrationTime;
    BankRequisites requisites;
    ImageResponseDto imageResponseDto;
}
