package ru.yandex.workshop.main.model.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.vendor.Vendor;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    Image image;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;
    @Enumerated(EnumType.STRING)
    License license;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    Vendor vendor;
    @Column(name = "seller_id")
    Long sellerId;
    Float price;
    Boolean installation;
    Integer quantity;
}
