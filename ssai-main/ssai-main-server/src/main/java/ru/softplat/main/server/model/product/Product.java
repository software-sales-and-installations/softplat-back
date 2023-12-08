package ru.softplat.main.server.model.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.server.model.image.Image;
import ru.softplat.main.server.model.seller.Seller;
import ru.softplat.main.server.model.vendor.Vendor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product", schema = "public")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "version")
    String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "production_time")
    LocalDateTime productionTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    Image image;

    @OneToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "has_demo")
    Boolean hasDemo;

    @OneToOne
    @JoinColumn(name = "vendor_id")
    Vendor vendor;

    @OneToOne
    @JoinColumn(name = "seller_id")
    Seller seller;

    @Column(name = "price")
    Float price;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "installation")
    Boolean installation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ProductStatus productStatus;

    @Column(name = "availability")
    Boolean productAvailability;

    @Column(name = "installation_price")
    Float installationPrice;
}
