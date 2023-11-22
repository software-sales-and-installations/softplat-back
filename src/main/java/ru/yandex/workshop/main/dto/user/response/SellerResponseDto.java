package ru.yandex.workshop.main.dto.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.model.seller.BankRequisites;

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
