package wid.bmsbackend.dto;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
