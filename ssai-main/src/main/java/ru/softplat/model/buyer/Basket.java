package ru.softplat.model.buyer;

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
@Builder
@Table(name = "basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "buyer_id")
    Long buyerId;
    @OneToMany
    @JoinColumn(name = "basket_id")
    List<BasketPosition> productsInBasket = new ArrayList<>();
}
