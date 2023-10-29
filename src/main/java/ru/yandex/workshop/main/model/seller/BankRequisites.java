package ru.yandex.workshop.main.model.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requisites")
public class BankRequisites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String account;
}
