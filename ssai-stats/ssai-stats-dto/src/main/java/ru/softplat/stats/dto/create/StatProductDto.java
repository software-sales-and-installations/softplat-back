package ru.softplat.stats.dto.create;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatProductDto {
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotNull
    StatSellerDto seller;
}
