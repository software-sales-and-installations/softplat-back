package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorDto {
    @NotBlank
    String name;
    @NotBlank
    String description;
    Long imageId;
    @NotBlank
    String country;
}