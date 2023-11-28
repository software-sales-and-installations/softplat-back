package ru.softplat.model;

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
@Table(name = "statistic", schema = "public")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    Buyer buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "date_buy")
    LocalDateTime dateBuy;

    @Column(name = "quantity")
    Long quantity;

    @Column(name = "amount")
    Double amount;
}