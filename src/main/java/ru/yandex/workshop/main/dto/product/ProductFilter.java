package ru.yandex.workshop.main.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilter {
    @Pattern(regexp = "[а-яА-Яa-zA-Z0-9\\s-$]")
    @Size(min = 3, max = 40, message = "Количество символов от 3 до 40 включительно")
    private String text;
    private List<Long> categories;
    private List<Long> sellerIds;
    private List<Long> vendorIds;
    private Boolean isRussian;
    private Float priceMin;
    private Float priceMax;
}

