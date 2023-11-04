package ru.yandex.workshop.security.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.image.Image;
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
    String description;
    LocalDateTime registrationTime;
    BankRequisites requisites;
    Image image;
}
