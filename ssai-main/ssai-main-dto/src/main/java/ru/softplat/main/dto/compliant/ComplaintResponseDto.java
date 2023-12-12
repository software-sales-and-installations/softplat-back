package ru.softplat.main.dto.compliant;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintResponseDto {
    Long id;
    ComplaintReason reason;
    BuyerResponseDto buyer;
    ProductResponseDto product;
    LocalDateTime createdAt;
    ComplaintStatus complaintStatus;
}
