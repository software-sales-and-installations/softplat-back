package ru.softplat.model.buyer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.model.product.Product;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
