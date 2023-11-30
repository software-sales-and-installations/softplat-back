package ru.softplat.main.dto.vendor;

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
