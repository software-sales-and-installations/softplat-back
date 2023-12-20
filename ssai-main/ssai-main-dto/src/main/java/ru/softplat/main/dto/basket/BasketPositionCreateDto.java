package ru.softplat.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasketPositionCreateDto {
    @NotNull(message = "Необходимо ввести id продукта")
    private long productId;
    @NotNull(message = "Необходимо ввести количество продукта")
    @Min(1)
    private int quantity;
    @NotNull(message = "Необходимо ввести выбранный вариант: с установкой/без установки")
    private Boolean installation;
}
