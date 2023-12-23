package ru.softplat.main.server.model.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import ru.softplat.main.server.model.image.Image;

import javax.persistence.*;
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
    @DateTimeFormat
    @Column(name = "registration_time")
    LocalDateTime registrationTime;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisites_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    BankRequisites requisites;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    Image image;
}
