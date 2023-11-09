package ru.yandex.workshop.main.dto.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuyerResponseDto {
    Long id;
    String email;
    String name;
    String phone;
    LocalDateTime registrationTime;
}
