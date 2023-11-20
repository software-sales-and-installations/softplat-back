package ru.yandex.workshop.main.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "buyer")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String email;
    String name;
    @Column(name = "number")
    String phone;
    @DateTimeFormat
    @Column(name = "registration_time")
    LocalDateTime registrationTime;
}
