package ru.softplat.main.dto.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.image.ImageResponseDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;

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
    BankRequisitesResponseDto requisites;
    ImageResponseDto imageResponseDto;
}
