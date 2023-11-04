package ru.yandex.workshop.main.dto.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.dto.ImageDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerForResponse {
    Long id;
    String email;
    String name;
    String phone;
    LocalDateTime registrationTime;
    BankRequisitesDto requisites;
    String description;
    ImageDto imageDto;
}
