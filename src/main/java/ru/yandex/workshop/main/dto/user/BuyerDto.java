package ru.yandex.workshop.main.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDto {
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @Length(min = 6, max = 30, message = "Длина email должна быть от 6 до 30 символов")
    private String email;
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    private String name;
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String phone;
}
