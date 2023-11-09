package ru.yandex.workshop.stats.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsFilterAdmin {

    List<Long> categoriesIds;
    List<Long> sellerIds;
    List<Long> vendorIds;
}
