package ru.yandex.workshop.main.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @DateTimeFormat
    @Column(name = "production_time")
    LocalDateTime productionTime;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    Buyer buyer;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
    Integer quantity;
    Float amount;
}
