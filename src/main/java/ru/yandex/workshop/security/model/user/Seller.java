package ru.yandex.workshop.security.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "seller")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String email;
    String name;
    @Column(name = "number")
    String phone;
    String description;
    @DateTimeFormat
    @Column(name = "registration_time")
    LocalDateTime registrationTime;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisites_id")
    BankRequisites requisites;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    Image image;
    @NotBlank
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @Enumerated(EnumType.STRING)
    Status status;
}
