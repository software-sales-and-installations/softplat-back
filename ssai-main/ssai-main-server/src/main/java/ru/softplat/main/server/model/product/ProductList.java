package ru.softplat.main.server.model.product;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductList {
    private List<Product> products;
    private Long count;
}
