package ru.softplat.main.dto.compliant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintResponseDto {
    Long id;
    ComplaintReason reason;
    BuyerResponseDto buyer;
    ProductResponseDto product;
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    ComplaintStatus complaintStatus;
}
