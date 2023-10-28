package ru.yandex.workshop.main.model.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String email;
    String name;
    @Column(name = "number")
    String telephone;
    @DateTimeFormat
    @Column(name = "registration_time")
    LocalDateTime registrationTime;
    @Column(name = "requisites_id")
    Long requisitesId;
}
