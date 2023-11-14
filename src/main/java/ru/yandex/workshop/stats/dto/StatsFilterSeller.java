package ru.yandex.workshop.stats.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.exception.NotValidValueException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.workshop.main.message.ExceptionMessage.NOT_VALID_VALUE_EXCEPTION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsFilterSeller {

    List<Long> categoriesIds;
    List<Long> vendorIds;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end;

    public static StatsFilterSeller of(
            List<Long> categoriesIds,
            List<Long> vendorIds,
            LocalDateTime start,
            LocalDateTime end
    ) {
        StatsFilterSeller filterSeler = new StatsFilterSeller();

        if (end.isAfter(LocalDateTime.now()) || start.isAfter(end)) {
            throw new NotValidValueException(NOT_VALID_VALUE_EXCEPTION.label);
        }
        if (start != null) {
            filterSeler.setStart(start);
        }
        if (end != null) {
            filterSeler.setEnd(end);
        }
        if (categoriesIds != null) {
            filterSeler.setCategoriesIds(categoriesIds);
        }
        if (vendorIds != null) {
            filterSeler.setVendorIds(vendorIds);
        }
        return filterSeler;
    }

    public boolean hasCategories() {
        return categoriesIds != null && !categoriesIds.isEmpty();
    }

    public boolean hasVendor() {
        return vendorIds != null && !vendorIds.isEmpty();
    }
}
