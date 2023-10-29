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
@Table(name = "product_basket")
public class ProductBasket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "basket_id")
    Long basketId;
    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;
    Integer quantity;
    //цена 1 единицы продукта
    Float price;
}
