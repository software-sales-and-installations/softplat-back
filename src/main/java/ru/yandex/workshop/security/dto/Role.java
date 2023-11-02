package ru.yandex.workshop.security.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    SELLER(Set.of(Permission.VENDORS_PUBLIC)), //TODO permissions
    BUYER(Set.of(Permission.VENDORS_PUBLIC)),
    ADMIN(Set.of(Permission.VENDORS_ADMIN, Permission.VENDORS_PUBLIC));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
