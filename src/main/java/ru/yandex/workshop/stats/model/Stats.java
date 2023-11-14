package ru.yandex.workshop.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = Stats.TABLE_STATS, schema = Stats.SCHEMA_TABLE)
public class Stats {

    public static final String TABLE_STATS = "statistic";
    public static final String SCHEMA_TABLE = "public";

    public static final String STATS_ID = "id";
    public static final String BUYER_ID = "buyer_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String DATE_BUY = "date_buy";
    public static final String QUANTITY = "quantity";
    public static final String AMOUNT = "amount";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = STATS_ID)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BUYER_ID)
    Buyer buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PRODUCT_ID)
    Product product;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = DATE_BUY)
    LocalDateTime dateBuy;

    @Column(name = QUANTITY)
    Integer quantity;

    @Column(name = AMOUNT)
    Float amount;
}