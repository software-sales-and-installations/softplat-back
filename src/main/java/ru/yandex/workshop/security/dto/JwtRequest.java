package ru.yandex.workshop.security.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.security.model.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {
    String email;
    String password;
    Role role;
}