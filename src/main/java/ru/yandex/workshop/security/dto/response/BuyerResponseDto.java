package ru.yandex.workshop.security.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.yandex.workshop.security.model.Buyer;

import java.util.Set;

@Data
@Builder
public class BuyerResponseDto implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String telephone;
    private Set<SimpleGrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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

    public static UserDetails fromUser(Buyer buyer) {
        return new User(
                buyer.getEmail(),
                buyer.getPassword(),
                buyer.getRole().getAuthorities()
        );
    }
}
