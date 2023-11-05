package ru.yandex.workshop.main.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = Product.TABLE_PRODUCTS, schema = Product.SCHEMA_TABLE)
public class Product {

    public static final String TABLE_PRODUCTS = "product";
    public static final String SCHEMA_TABLE = "public";

    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_VERSION = "version";
    public static final String PRODUCT_TIME = "production_time";
    public static final String IMAGE_ID = "image_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String PRODUCT_LICENSE = "license";
    public static final String VENDOR_ID = "vendor_id";
    public static final String SELLER_ID = "seller_id";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_INSTALLATION = "installation";
    public static final String PRODUCT_STATUS = "status";
    public static final String PRODUCT_AVAILABILITY = "availability";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PRODUCT_ID)
    Long id;

    @Column(name = PRODUCT_NAME)
    String name;

    @Column(name = PRODUCT_DESCRIPTION)
    String description;

    @Column(name = PRODUCT_VERSION)
    String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = PRODUCT_TIME)
    LocalDateTime productionTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = IMAGE_ID)
    Image image;

    @OneToOne
    @JoinColumn(name = CATEGORY_ID)
    Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = PRODUCT_LICENSE)
    License license;

    @OneToOne
    @JoinColumn(name = VENDOR_ID)
    Vendor vendor;

    @OneToOne
    @JoinColumn(name = SELLER_ID)
    Seller seller;

    @Column(name = PRODUCT_PRICE)
    Float price;

    @Column(name = PRODUCT_QUANTITY)
    Integer quantity;

    @Column(name = PRODUCT_INSTALLATION)
    Boolean installation;

    @Enumerated(EnumType.STRING)
    @Column(name = PRODUCT_STATUS)
    ProductStatus productStatus;

    @Column(name = PRODUCT_AVAILABILITY)
    Boolean productAvailability;
}
