package ru.yandex.workshop.main.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.main.dto.validation.New;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerDto {
    @NotBlank(groups = {New.class}, message = "Необходимо указать имя")
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    private String name;
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
    @NotBlank(groups = {New.class}, message = "Необходимо указать номер телефона")
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String phone;
    @NotBlank
    @Length(max = 500, message = "Описание должно быть длинной не более 500 символов")
    String description;
}
