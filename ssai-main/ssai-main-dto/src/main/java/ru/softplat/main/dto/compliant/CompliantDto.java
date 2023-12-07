package ru.softplat.main.dto.compliant;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;
import ru.softplat.main.dto.user.response.SellerResponseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompliantDto {
    Long id;
    String reason;
    BuyerResponseDto buyer;
    ProductResponseDto product;
    SellerResponseDto seller;
    LocalDateTime createdAt;
}
