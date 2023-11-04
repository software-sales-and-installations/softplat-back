package ru.yandex.workshop.security.dto.registration;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.Status;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RegistrationAdminDto {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    String email;
    @NotBlank(message = "Необходимо указать имя")
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    String name;
    String password;
    String confirmPassword;
    @Builder.Default
    Role role = Role.ADMIN;
    @Builder.Default
    Status status = Status.ACTIVE;
}
