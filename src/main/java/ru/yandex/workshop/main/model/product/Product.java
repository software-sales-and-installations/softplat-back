package ru.yandex.workshop.main.model.product;

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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    String version;
    @DateTimeFormat
    @Column(name = "production_time")
    LocalDateTime productionTime;
    @Column(name = "image_id")
    Long imageId;
    @Column(name = "category_id")
    Long categoryId;
    @Enumerated(EnumType.STRING)
    License license;
    @Column(name = "vendor_id")
    Long vendorId;
    @Column(name = "seller_id")
    Long sellerId;
    Float price;
}
