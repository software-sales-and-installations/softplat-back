package ru.softplat.stats.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsFilterSeller {

    @NotNull(message = "Введите корректную дату начала")
    @Past(message = "Введите корректную дату начала")
    LocalDate start;
    @NotNull(message = "Введите корректную дату конца")
    LocalDate end;
}
