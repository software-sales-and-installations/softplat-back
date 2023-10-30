package ru.yandex.workshop.main.dto.buyer;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDto {
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @Size(min = 6, max = 30, message = "Длина email должна быть от 6 до 30 символов")
    private String email;
    @NotBlank(message = "Необходимо указать имя пользователя")
    @Size(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    private String firstName;
    @NotBlank(message = "Необходимо указать фамилию пользователя")
    @Size(min = 2, max = 20, message = "Длина фамилии должна быть от 2 до 20 символов")
    private String lastName;
    @NotBlank(message = "Необходимо указать номер телефона")
    @Pattern(regexp = "[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String telephone;
}
