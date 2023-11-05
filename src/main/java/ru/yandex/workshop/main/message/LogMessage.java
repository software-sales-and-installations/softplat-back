package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_ADD_BUYER("Попытка добавления нового покупателя."),
    TRY_PATCH_BUYER("Попытка изменения покупателя по id: {}"),
    TRY_GET_BUYER("Попытка получения покупателя по id: {}"),
    TRY_ADD_SELLER("Попытка добавления нового продавца."),
    TRY_ADD_SELLER_IMAGE("Попытка добавления нового изображения продавца."),
    TRY_DELETE_SELLER_IMAGE("Попытка удаления изображения продавца."),
    TRY_PATCH_SELLER("Попытка изменения продавца по email: {}"),
    TRY_GET_SELLER("Попытка получения продавца по email: {}"),
    TRY_SELLER_PATCH_REQUISITES("Попытка изменения реквизитов по email: {}"),
    TRY_SELLER_GET_REQUISITES("Попытка получения реквизитов по email: {}"),
    TRY_SELLER_DELETE_REQUISITES("Попытка удаления реквизитов по email: {}"),
    TRY_SELLER_ADD_PRODUCT_IMAGE("Попытка добавления нового изображения продукта."),
    TRY_SELLER_DELETE_PRODUCT_IMAGE("Попытка удаления изображения продукта."),
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_ADD_VENDOR_IMAGE("Попытка добавления нового изображения вендора."),
    TRY_ADMIN_DElETE_VENDOR_IMAGE("Попытка удаления изображения вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_GET_VENDOR("Попытка получения списка вендоров."),
    TRY_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    IMAGE_UPLOADING_ERROR("Ошибка при загрузке изображения"),
    TRY_GET_ALL_PRODUCTS_SELLER("Попытка получения всех продуктов продавцов."),
    TRY_GET_PRODUCTS_ADMIN("Попытка получения админом всех продуктов продавца (URL/admin) по id: {}."),
    TRY_GET_PRODUCTS_BY_ID_ADMIN("Попытка получения продукта по id: {}."),
    TRY_UPDATE_STATUS_PRODUCT_ON_PUBLISHED("Попытка установки статуса 'PUBLISHED' продукту id: {}."),
    TRY_UPDATE_STATUS_PRODUCT_ON_REJECTED("Попытка установки статуса 'REJECTED' продукту id: {}."),
    TRY_DELETE_PRODUCT_ADMIN("Попытка удаления продукта по id: {}."),
    TRY_GET_PRODUCTS_SELLER("Попытка получения всех своих продуктов продавцом (URL/seller) id: {}."),
    TRY_GET_PRODUCT_BY_ID("Попытка получения продукта id: {} продавца id: {}."),
    TRY_CREATE_PRODUCT("Попытка создать продукт: {}."),
    TRY_UPDATE_PRODUCT("Попытка обновления продукта id: {} продавца id: {}: {}"),
    TRY_UPDATE_STATUS_PRODUCT_ON_SENT("Попытка установки статуса 'SHIPPED' продукту id: {} продавцом: {}.");

    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}
