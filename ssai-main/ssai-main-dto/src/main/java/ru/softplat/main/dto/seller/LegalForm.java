package ru.softplat.main.dto.seller;

public enum LegalForm {
    IP("Индивидуальный предприниматель"),
    NAO("Непубличное акционерное общество"),
    PAO("Публичное акционерное общество"),
    OOO("Общество с ограниченной ответственностью"),
    OAO("Открытое акционерное общество"),
    ZAO("Закрытое акционерное общество");

    public final String label;

    LegalForm(String label) {
        this.label = label;
    }
}
