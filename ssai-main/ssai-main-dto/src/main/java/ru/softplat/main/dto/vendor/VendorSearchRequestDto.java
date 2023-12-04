package ru.softplat.main.dto.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VendorSearchRequestDto {
    String text;
    List<Country> countries;
}
