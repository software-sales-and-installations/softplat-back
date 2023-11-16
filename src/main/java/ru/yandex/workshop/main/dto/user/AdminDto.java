package ru.yandex.workshop.main.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    @NotBlank(message = "Необходимо указать имя")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,20}$", message = "Длина имени пользователя должна быть от 2 до 20 символов.")
    private String name;
    @NotBlank(message = "Необходимо указать email")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @Length(min = 6, max = 30, message = "Длина почты должна быть от 6 до 30 символов.")
    private String email;
}
