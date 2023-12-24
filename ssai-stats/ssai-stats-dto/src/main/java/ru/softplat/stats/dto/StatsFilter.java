package ru.softplat.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsFilter {

    List<Long> sellerIds;
    @NotNull(message = "Введите корректную дату начала")
    @Past(message = "Введите корректную дату начала")
    LocalDate start;
    @NotNull(message = "Введите корректную дату конца")
    LocalDate end;
}
