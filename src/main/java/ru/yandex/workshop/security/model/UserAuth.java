package ru.yandex.workshop.security.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.security.dto.Role;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_details")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String email;
    String password;
    Role role;
}
