package ru.softplat.security.server.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.softplat.security.server.model.New;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtAuthRequest {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @Length(min = 6, max = 30, message = "Адрес электронной почты должен содержать от 6 до 30 символов")
    String email;
    @NotBlank(message = "Необходимо указать пароль")
    @Pattern(groups = {New.class}, regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,40}$", message = "Пароль должен соответствовать следующим требованиям: " +
            "1. Использование строчной буквы;" +
            "2. Использование прописной буквы;" +
            "3. Использование спец.символа \"@,#,$,%,^,&,+,=,!\";" +
            "4. Использование цифры от 0 до 9;" +
            "5. Длина пароля от 8 до 40 символов;")
    String password;
    @NotBlank(groups = {New.class}, message = "Необходимо указать пароль повторно")
    String confirmPassword;
}