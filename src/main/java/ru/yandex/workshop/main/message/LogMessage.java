package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_ADMIN_ADD_BUYER("Попытка добавления нового покупателя."),
    TRY_ADMIN_PATCH_BUYER("Попытка изменения покупателя по id: {}"),
    TRY_ADMIN_GET_BUYER("Попытка получения покупателя по id: {}"),
    ADMIN_ADD_BUYER("Успешное добавление нового покупателя."),
    ADMIN_PATCH_BUYER("Успешное изменение покупателя по id: {}"),
    ADMIN_GET_BUYER("Успешное получение покупателя по id: {}"),
    TRY_ADMIN_ADD_SELLER("Попытка добавления нового продавца."),
    TRY_ADMIN_PATCH_SELLER("Попытка изменения продавца по email: {}"),
    TRY_ADMIN_GET_SELLER("Попытка получения продавца по email: {}"),
    ADMIN_ADD_SELLER("Успешное добавление нового продавца."),
    ADMIN_PATCH_SELLER("Успешное изменение продавца по email: {}"),
    ADMIN_GET_SELLER("Успешное получение продавца по email: {}"),
    TRY_SELLER_PATCH_REQUISITES("Попытка изменения реквизитов по email: {}"),
    TRY_SELLER_GET_REQUISITES("Попытка получения реквизитов по email: {}"),
    TRY_SELLER_DELETE_REQUISITES("Попытка удаления реквизитов по email: {}"),
    SELLER_PATCH_REQUISITES("Успешное изменение реквизитов по email: {}"),
    SELLER_GET_REQUISITES("Успешное получение реквизитов по email: {}"),
    SELLER_DELETE_REQUISITES("Успешное удаление реквизитов по email: {}"),
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_GET_VENDOR("Попытка получения списка вендоров."),
    TRY_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    ADMIN_ADD_VENDOR("Успешное добавление нового вендора."),
    ADMIN_PATCH_VENDOR("Успешное изменение вендора."),
    GET_VENDOR("Успешное получение списка вендоров."),
    GET_ID_VENDOR("Успешное получение вендора по id: {}."),
    ADMIN_DELETE_VENDOR("Успешное удаление вендора по id: {}.");
    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}
