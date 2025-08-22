package wid.bmsbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wid.bmsbackend.utils.TimeUtil;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private final SecretKey secretKey;
    private final TimeUtil timeUtil;

    public JwtService(@Value("${jwt.secret}") String secretKey, TimeUtil timeUtil) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.timeUtil = timeUtil;
    }

    public String generateAccessToken(String username) {
        long systemTimeMillis = timeUtil.getCurrentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(systemTimeMillis))
                .expiration(new Date(systemTimeMillis + 1000 * 60 * 60 * 24))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        long systemTimeMillis = timeUtil.getCurrentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(systemTimeMillis))
                .expiration(new Date(systemTimeMillis + 1000 * 60 * 60 * 24 * 7))
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getPayload(token)
                .getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = getUsernameFromToken(token);
            boolean tokenExpired = isTokenExpired(token);
            return extractedUsername.equals(username) && !tokenExpired;
        } catch (Exception e) {
            log.warn("Token is invalid: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return getPayload(token)
                .getExpiration()
                .before(new Date());
    }


    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build().parseSignedClaims(token).getPayload();
    }
}
