package ru.softplat.security.server.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.Update;
import ru.softplat.security.server.web.validation.NotPopularPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassRequest {
    @NotBlank(groups = {Update.class}, message = "Необходимо указать адрес электронной почты")
    String email;
    @NotBlank(groups = {Update.class}, message = "Необходимо указать пароль")
    String oldPass;
    @NotBlank(groups = {New.class, Update.class}, message = "Необходимо указать пароль")
    @Pattern(groups = {New.class, Update.class}, regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,40}$", message = "Пароль должен соответствовать следующим требованиям: " +
            "1. Использование строчной буквы;" +
            "2. Использование прописной буквы;" +
            "3. Использование спец.символа \"@,#,$,%,^,&,+,=,!\";" +
            "4. Использование цифры от 0 до 9;" +
            "5. Длина пароля от 8 до 40 символов;")
    @NotPopularPassword(groups = {New.class, Update.class})
    String password;
    @NotBlank(groups = {New.class, Update.class}, message = "Необходимо указать пароль повторно")
    String confirmPassword;
}