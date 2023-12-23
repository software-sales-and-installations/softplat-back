package ru.softplat.main.server.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.server.model.product.Product;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "basket_position")
public class BasketPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "basket_id")
    Long basketId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
    Integer quantity;
    Boolean installation;
}
