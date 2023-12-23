package ru.softplat.main.dto.comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.Update;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreateUpdateDto {
    @Pattern(regexp = "[0-9a-zA-Zа-яА-Я-@#$.,%^&+=!\\s]{2,600}$",
            message = "Длина текста в отзыве должна быть от 2 до 600 символов.",
            groups = {New.class, Update.class})
    String text;

    @DecimalMin(value = "0.5", message = "Оценка должна быть выше или равна 0.5", groups = {New.class, Update.class})
    @DecimalMax(value = "5.0", message = "Оценка должна быть ниже или равна 5.0", groups = {New.class, Update.class})
    @NotNull(groups = {New.class}, message = "Необходимо указать оценку товара от 1 до 5.")
    Float rating;
}
