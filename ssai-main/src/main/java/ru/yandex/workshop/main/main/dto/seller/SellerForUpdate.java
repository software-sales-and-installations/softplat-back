package ru.yandex.workshop.main.main.dto.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.main.main.dto.ImageDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerForUpdate {
    @Email(message = "Email должен быть корректным адресом электронной почты")
    String email;
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    String name;
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    String phone;
    @Length(max = 500, message = "Описание должно быть длинной не более 500 символов")
    String description;
    ImageDto imageDto;
}
