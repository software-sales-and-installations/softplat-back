package ru.yandex.workshop.main.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.product.Product;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    Buyer buyer;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    Product product;
}
