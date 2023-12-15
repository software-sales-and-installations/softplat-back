package ru.softplat.main.dto.compliant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintUpdateDto {
    @NotNull(message = "Необходимо указать статус: REVIEW – направить на ревью селлеру, или CLOSED – жалоба закрыта.")
    ComplaintStatus status;
    @NotNull(groups = {Review.class}, message = "Необходимо указать комментарий для продавца по доработке карточки товара.")
    String comment;

    public interface Review {}
}
