package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.vendor.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorDto {
    @NotBlank
    String name;
    @NotBlank
    String description;
    Long imageId; //TODO
    @NotNull
    Country country;
}