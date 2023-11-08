package ru.yandex.workshop.main.dto.user;

import lombok.*;
import ru.yandex.workshop.main.dto.validation.New;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDto {
    @Email(groups = {New.class}, message = "Email должен быть корректным адресом электронной почты")
    @Size(min = 6, max = 30, message = "Длина email должна быть от 6 до 30 символов")
    private String email;
    @NotBlank(groups = {New.class}, message = "Необходимо указать имя пользователя")
    @Size(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    private String name;
    @NotBlank(groups = {New.class}, message = "Необходимо указать номер телефона")
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String phone;
}