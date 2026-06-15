package it.academy.largesystems.eventhub.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtProvider {

    private final String SECRET_STRING = "EventHubApplicationSecureJwtSigningKeyForHmacSha256Encryption2026";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .claims(Map.of("roles", user.getAuthorities()))
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        List<?> roles = claims.get("roles", List.class);

        if (roles == null) {
            return authoritiesList;
        }

        for (Object role : roles) {
            if (role instanceof Map) {
                Map<?, ?> roleMap = (Map<?, ?>) role;
                String authorityName = (String) roleMap.get("authority");
                authoritiesList.add(new SimpleGrantedAuthority(authorityName));
            }
        }

        return authoritiesList;
    }
}