package ru.yandex.workshop.main.dto.product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchProductRequest {
    String name;
    String description;
    Long categories;
    Integer startPrice;
    Integer endPrice;
    Integer from;
    Integer size;

    public static SearchProductRequest of(String name,
                                          String description,
                                          Long categories,
                                          Integer startPrice,
                                          Integer endPrice,
                                          Integer from,
                                          Integer size) {
        SearchProductRequest request = new SearchProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setCategories(categories);
        request.setStartPrice(startPrice);
        request.setEndPrice(endPrice);
        request.setFrom(from);
        request.setSize(size);
        return request;
    }
}
