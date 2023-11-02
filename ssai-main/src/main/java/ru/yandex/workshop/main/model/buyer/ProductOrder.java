package ru.yandex.workshop.main.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.product.Product;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_order")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "order_id")
    Long orderId;
    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;
    Integer quantity;
    //стоимость 1 единицы продукта с учетом скидок/бонусов/промокодов/цены за установку
    @Column(name = "amount")
    Float productAmount;
}
