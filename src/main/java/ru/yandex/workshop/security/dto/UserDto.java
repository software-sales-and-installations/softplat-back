package ru.yandex.workshop.security.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.Status;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    String email;
    @NotNull(message = "Необходимо указать пароль")
    String password;
    @NotNull(message = "Необходимо повторно указать пароль")
    String confirmPassword;
    @NotBlank(message = "Необходимо указать имя")
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    String name;
    String phone;
    @Length(max = 500, message = "Описание должно быть длинной не более 500 символов")
    String description;
    @NotNull(message = "Необходимо выбрать роль пользователя: админ/покупатель/продавец")
    Role role;
    @Builder.Default
    Status status = Status.ACTIVE;
}
