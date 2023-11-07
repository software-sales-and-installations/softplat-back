package ru.yandex.workshop.security.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.user.Admin;
import ru.yandex.workshop.security.model.user.Buyer;
import ru.yandex.workshop.security.model.user.Seller;

import java.util.Collection;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSecurity implements UserDetails {
    Long id;
    String email;
    String password;
    Set<SimpleGrantedAuthority> authorities;
    boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(Admin admin) {
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getStatus().equals(Status.ACTIVE),
                admin.getRole().getAuthorities()
        );
    }

    public static UserDetails fromUser(Seller seller) {
        return new User(
                seller.getEmail(),
                seller.getPassword(),
                seller.getStatus().equals(Status.ACTIVE),
                seller.getStatus().equals(Status.ACTIVE),
                seller.getStatus().equals(Status.ACTIVE),
                seller.getStatus().equals(Status.ACTIVE),
                seller.getRole().getAuthorities()
        );
    }

    public static UserDetails fromUser(Buyer buyer) {
        return new User(
                buyer.getEmail(),
                buyer.getPassword(),
                buyer.getStatus().equals(Status.ACTIVE),
                buyer.getStatus().equals(Status.ACTIVE),
                buyer.getStatus().equals(Status.ACTIVE),
                buyer.getStatus().equals(Status.ACTIVE),
                buyer.getRole().getAuthorities()
        );
    }
}
