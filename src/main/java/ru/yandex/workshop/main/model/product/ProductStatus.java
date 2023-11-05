package ru.yandex.workshop.main.model.product;

public enum ProductStatus {
    DRAFT("Черновик"),
    SHIPPED("Отправлено"),
    REJECTED("Отклонено"),
    PUBLISHED("Опубликовано");

    public final String label;

    ProductStatus(String label) {
        this.label = label;
    }
}
