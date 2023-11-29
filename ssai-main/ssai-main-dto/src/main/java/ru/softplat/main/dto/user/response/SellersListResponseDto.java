package ru.softplat.main.dto.user.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellersListResponseDto {
    List<SellerResponseDto> sellers;
}
