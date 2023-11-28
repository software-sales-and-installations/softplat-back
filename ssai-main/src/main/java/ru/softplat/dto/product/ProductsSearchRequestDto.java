package ru.softplat.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softplat.model.product.License;
import ru.softplat.model.vendor.Country;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsSearchRequestDto {
    @Pattern(regexp = "^[\\p{L}\\d\\s-]+$")
    @Size(min = 3, max = 40, message = "Количество символов от 3 до 40 включительно")
    private String text;
    private List<Long> categories;
    private List<Long> sellerIds;
    private List<Long> vendorIds;
    private List<Country> countries;
    private List<License> licenses;
    @PositiveOrZero
    private Float priceMin;
    @PositiveOrZero
    private Float priceMax;
}

