package ru.yandex.workshop.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.service.AdminDetailsServiceImpl;
import ru.yandex.workshop.security.service.BuyerDetailsServiceImpl;
import ru.yandex.workshop.security.service.SellerDetailsServiceImpl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
@PropertySource(value = {"classpath:application.properties"})
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final AdminDetailsServiceImpl adminDetailsService;
    private final SellerDetailsServiceImpl sellerDetailsService;
    private final BuyerDetailsServiceImpl buyerDetailsService;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.header}")
    private String authHeader;
    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateToken(String userDetails, String role) {
        Claims claims = Jwts.claims().setSubject(userDetails);
        claims.put("role", role);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (RuntimeException e) {
            throw new RuntimeException("Время токена истекло.");
        }
    }

    public Authentication getAuthentication(String token) {

        if (adminDetailsService.loadUserByUsername(getUsername(token)) != null) {
            UserDetails userDetails = adminDetailsService.loadUserByUsername(getUsername(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        } else if (sellerDetailsService.loadUserByUsername(getUsername(token)) != null) {
            UserDetails userDetails = sellerDetailsService.loadUserByUsername(getUsername(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        } else if (buyerDetailsService.loadUserByUsername(getUsername(token)) != null) {
            UserDetails userDetails = buyerDetailsService.loadUserByUsername(getUsername(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        }
        return null;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authHeader);
    }

}
