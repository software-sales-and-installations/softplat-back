package ru.yandex.workshop.main.dto.basket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponseDto {
    Long id;
    BuyerResponseDto buyer;
    List<OrderPositionResponseDto> productsOrdered;
    Float orderCost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime productionTime;
}
