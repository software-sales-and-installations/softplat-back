package ru.softplat.main.dto.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorsListResponseDto {
    List<VendorResponseDto> vendors;
}
