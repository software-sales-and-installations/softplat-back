package ru.yandex.workshop.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationUserDto {
    @NotBlank(message = "Необходимо указать имя")
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    private String name;
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
    @NotBlank(message = "Необходимо указать номер телефона")
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String phone;
    private String password;
    private String confirmPassword;
    @NotNull(message = "Необходимо выбрать роль пользователя")
    private Role role;
}
