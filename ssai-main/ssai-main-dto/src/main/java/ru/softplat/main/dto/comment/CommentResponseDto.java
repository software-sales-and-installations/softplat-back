package ru.softplat.main.dto.comment;

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
public class CommentResponseDto {
    Long id;
    BuyerResponseDto author;
    ProductResponseDto product;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#.#")
    Float rating;
    String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime date;
}
