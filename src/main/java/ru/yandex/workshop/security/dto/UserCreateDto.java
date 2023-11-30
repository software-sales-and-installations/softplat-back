package ru.yandex.workshop.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.security.validation.NotPopularPassword;
import ru.yandex.workshop.security.validation.PasswordNotContainsEmail;
import ru.yandex.workshop.security.validation.PasswordSameConfirmPassword;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordNotContainsEmail
@PasswordSameConfirmPassword
public class UserCreateDto {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Pattern(regexp = "^([a-zA-Z0-9._-]){2,12}+(@[a-zA-Z]{2,8})+(\\.[a-zA-Z]{2,3})$", message = "Длина почты должна быть от 2 до 30 символов.")
    String email;
    @NotPopularPassword
    @NotBlank(message = "Необходимо указать пароль")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-@#$%^&+=!])(?=\\S+$).{8,40}$", message = "Пароль должен соответствовать следующим требованиям: " +
            "1. Использование строчной буквы;" +
            "2. Использование прописной буквы;" +
            "3. Использование спец.символа \"@,#,$,%,^,&,+,=,!\";" +
            "4. Использование цифры от 0 до 9;" +
            "5. Длина пароля от 8 до 40 символов;")
    String password;
    @NotBlank(message = "Необходимо повторно указать пароль")
    String confirmPassword;
    @NotBlank(message = "Необходимо указать имя")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я-\\s]{2,20}$", message = "Длина имени пользователя должна быть от 2 до 20 символов. Цифры в имени не допускаются.")
    String name;
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    String phone;
    @NotNull(message = "Необходимо выбрать роль пользователя: админ/покупатель/продавец")
    Role role;
    @Schema(description = "Вспомогательный параметр, не указывается в теле запроса")
    Status status;
}
