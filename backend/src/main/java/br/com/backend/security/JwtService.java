package br.com.backend.security;

import br.com.backend.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "sua-chave-super-secreta-com-no-minimo-32-caracteres";

    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        Instant now = Instant.now();

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(key)
                .compact();

        return token;
    }

    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
