package wid.bmsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import wid.bmsbackend.dto.*;
import wid.bmsbackend.exception.ExceptionCodeConstants;
import wid.bmsbackend.exception.InvalidTokenException;
import wid.bmsbackend.service.CustomUserDetailsService;
import wid.bmsbackend.service.JwtService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        // authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        final String jwt = jwtService.generateAccessToken(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(new AuthenticationResponse(jwt)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String token = request.refreshToken();
        String username = jwtService.getUsernameFromToken(token);
        if (jwtService.isTokenValid(token, username)) {
            String accessToken = jwtService.generateAccessToken(username);
            String refreshToken = jwtService.generateRefreshToken(username);
            return ResponseEntity.ok(ApiResponse.success(new RefreshTokenResponse(accessToken, refreshToken)));
        }
        throw new InvalidTokenException("Invalid token");
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidTokenException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("invalid authentication request", e.getMessage(), ExceptionCodeConstants.INVALID_TOKEN));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.badRequest().body(ApiResponse.error("InvalidTokenException", e.getMessage(), ExceptionCodeConstants.INVALID_TOKEN));
    }
}
