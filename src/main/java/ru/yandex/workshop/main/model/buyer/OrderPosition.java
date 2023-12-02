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
@Table(name = "order_position")
public class OrderPosition {
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
    Float productCost;
    Boolean installation;

    @Override
    public String toString() {
        String installString = installation ? "Да" : "Нет";
        return  "\n" +
                "Название товара: " + product.getName() + "\n" +
                "Количество товара: " + quantity + " шт.\n" +
                "Стоимость 1 единицы товара: " + productCost + " руб.\n" +
                "Установка: " + installString + "\n";
    }
}
