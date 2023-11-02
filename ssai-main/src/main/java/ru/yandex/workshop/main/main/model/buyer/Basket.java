package ru.yandex.workshop.main.main.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    Buyer buyer;
    @OneToMany
    @JoinColumn(name = "basket_id")
    List<ProductBasket> productsInBasket = new ArrayList<>();
}
