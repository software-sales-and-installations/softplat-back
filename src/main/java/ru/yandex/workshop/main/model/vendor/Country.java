package ru.yandex.workshop.main.model.vendor;

import ru.yandex.workshop.main.exception.CountryNotFoundException;

import java.util.Arrays;

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
}
