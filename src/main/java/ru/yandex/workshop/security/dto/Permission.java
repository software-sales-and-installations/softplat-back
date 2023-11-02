package ru.yandex.workshop.security.dto;

public enum Permission {
    VENDORS_PUBLIC("vendors:read"),
    VENDORS_ADMIN("vendors:admin");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
