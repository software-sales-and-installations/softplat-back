package ru.yandex.workshop.main.model.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.product.License;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    @Column(name = "image_id")
    Long imageId;
    @Enumerated(EnumType.STRING)
    Country country;
}
