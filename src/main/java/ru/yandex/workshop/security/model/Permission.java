package ru.yandex.workshop.security.model;

public enum Permission {
    SELLER("seller:write"),
    BUYER("buyer:write"),
    ADMIN("admin:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
