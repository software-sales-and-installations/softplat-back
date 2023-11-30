package ru.softplat.main.dto.product;

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
