package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "statistic_product")
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

    @Column(name = "date_buy")
    LocalDate dateBuy;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "profit")
    Double profit;

    @Column(name = "profit_seller")
    Double profitSeller;

    @Column(name = "profit_admin")
    Double profitAdmin;
}