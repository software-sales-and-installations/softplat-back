package ru.softplat.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "stat_product")
public class StatProduct {
    @Id
    Long id;
    String name;
    @OneToOne
    @JoinColumn(name = "seller_id")
    StatSeller seller;
}
