package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "statistic")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    StatBuyer buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    StatProduct product;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "date_buy")
    LocalDateTime dateBuy;

//    @Column(name = "demo_quantity")
//    Long demoQuantity;

    @Column(name = "quantity")
    Long quantity;

    @Column(name = "amount")
    Double amount;

    @Column(name = "receive_amount_seller")
    Double receiveAmountSeller;

    @Column(name = "receive_amount_admin")
    Double receiveAmountAdmin;
}