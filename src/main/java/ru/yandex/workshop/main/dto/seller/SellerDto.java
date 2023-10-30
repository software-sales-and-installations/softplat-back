package ru.yandex.workshop.main.dto.seller;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerDto {
    @NotBlank(message = "Необходимо указать имя")
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    private String name;
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
    @NotBlank(message = "Необходимо указать номер телефона")
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String phone;
}
