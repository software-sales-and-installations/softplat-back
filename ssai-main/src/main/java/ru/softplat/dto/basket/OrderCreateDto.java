package ru.softplat.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {
    @NotNull(message = "Выберите хотя бы один товар для покупки")
    private List<Long> basketPositionIds;
}