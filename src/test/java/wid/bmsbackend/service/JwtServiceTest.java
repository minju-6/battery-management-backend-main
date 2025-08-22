package wid.bmsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wid.bmsbackend.utils.TimeUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

class JwtServiceTest {

    @Test
    @DisplayName("토큰이 정상적으로 생성되는지 확인한다.")
    void generateAccessToken() {
        String username = "testUser";
        JwtService jwtService = getJwtService();
        String token = jwtService.generateAccessToken(username);
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("토큰에서 유저네임을 정상적으로 추출야 한다.")
    void getUsernameFromToken() {
        String username = "testUser";
        JwtService jwtService = getJwtService();
        String token = jwtService.generateAccessToken(username);
        String extractedUsername = jwtService.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("토큰이 유효한지 확인 한다.")
    void isTokenValid() {
        String username = "testUser";
        JwtService jwtService = getJwtService();
        String token = jwtService.generateAccessToken(username);
        boolean isValid = jwtService.isTokenValid(token, username);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("토큰이 유효하지 않은 경우 false를 반환한다.")
    void isTokenValidInvalid() {
        String username = "testUser";
        Clock clock = Clock.fixed(LocalDateTime.now().minusDays(2).atOffset(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
        JwtService jwtService = getJwtService(clock);
        String token = jwtService.generateAccessToken(username);
        boolean isValid = jwtService.isTokenValid(token, username);
        assertThat(isValid).isFalse();
    }

    private static JwtService getJwtService() {
        return getJwtService(null);
    }

    private static JwtService getJwtService(Clock clock) {
        String jwtKey = UUID.randomUUID().toString();
        TimeUtil timeUtil = new TimeUtil();
        if (clock != null) {
            timeUtil = new TimeUtil(clock);
        }
        return new JwtService(jwtKey, timeUtil);
    }
}