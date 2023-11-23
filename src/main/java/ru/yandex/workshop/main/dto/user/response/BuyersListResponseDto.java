package ru.yandex.workshop.main.dto.user.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuyersListResponseDto {
    List<BuyerResponseDto> buyers;
}
