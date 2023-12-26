package ru.softplat.main.dto.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.validation.BankRequisitesLegalForm;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@BankRequisitesLegalForm(groups = {New.class})
public class BankRequisitesCreateUpdateDto {
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9]{20}",
            message = "Номер счета должен содержать 20 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать номер счета")
    String account;
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9]{10,12}",
            message = "ИНН должен содержать 10 или 12 (ИП) цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать ИНН")
    String inn;
    @NotNull(groups = {New.class}, message = "Выберите правовую форму")
    LegalForm legalForm;
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9]{9}", message = "БИК должен содержать 9 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать БИК")
    String bik;
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9]{10}", message = "КПП должен содержать 10 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать КПП")
    String kpp;
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9]{13}", message = "ОГРН должен содержать 13 цифр 0-9")
    String ogrn;
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9]{15}", message = "ОГРНИП должен содержать 15 цифр 0-9")
    String ogrnip;
    @Pattern(groups = {New.class, Update.class}, regexp = "^[a-zA-Zа-яА-Я0-9-.,\\s]{5,500}$",
            message = "Адрес должен содержать не более 500 символов 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать юридический адрес")
    String address;
}
