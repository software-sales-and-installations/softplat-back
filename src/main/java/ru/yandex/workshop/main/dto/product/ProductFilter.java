package ru.yandex.workshop.main.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilter {
    private String text;
    private List<Long> categories;
    private List<Long> sellerIds;
    private List<Long> vendorIds;
}
