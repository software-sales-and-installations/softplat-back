package ru.yandex.workshop.security.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.yandex.workshop.security.model.Admin;

import java.util.Set;

@Data
@Builder
public class AdminResponseDto implements UserDetails {
    private Long id;
    private String email;
    private String name;
    private String password;
    private Set<SimpleGrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public static UserDetails fromUser(Admin admin) {
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                admin.getRole().getAuthorities()
        );
    }
}
