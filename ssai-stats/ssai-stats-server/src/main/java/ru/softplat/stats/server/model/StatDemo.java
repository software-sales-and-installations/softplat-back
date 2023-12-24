package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "statistic_demo")
public class StatDemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    StatBuyer buyer;
    @OneToOne
    @JoinColumn(name = "product_id")
    StatProduct product;
    Integer quantity;
}
