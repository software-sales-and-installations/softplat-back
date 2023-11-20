package ru.yandex.workshop.main.dto.user;

import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    @Pattern(regexp = "^[a-zA-Zа-яА-Я-\\s]{2,20}$", message = "Длина имени пользователя должна быть от 2 до 20 символов.")
    private String name;
    @Pattern(regexp = "^([a-zA-Z0-9._-]){2,12}+(@[a-zA-Z]{2,8})+(\\.[a-zA-Z]{2,3})$", message = "Длина почты должна быть от 2 до 30 символов.")
    private String email;
}
