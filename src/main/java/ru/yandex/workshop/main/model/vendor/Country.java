package ru.yandex.workshop.main.model.vendor;

import ru.yandex.workshop.main.exception.CountryNotFoundException;

import java.util.Arrays;
import java.util.Optional;

public enum Country {
    RUSSIA("Russia"),
    CHINA("China"),
    INDIA("India"),
    UK("UK"),
    USA("USA");

    public final String label;

    Country(String label) {
        this.label = label;
    }

    public static Country findCountry(String name){
        return Arrays.stream(values())
                .filter(country -> country.label.equals(name))
                .findFirst()
                .orElseThrow(() -> new CountryNotFoundException("Данная страна не найдена"));
    }
}
