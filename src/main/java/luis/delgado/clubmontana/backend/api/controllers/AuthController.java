package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import java.time.Duration;
import luis.delgado.clubmontana.backend.api.dtos.LoginRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.TokenResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.AuthControllerMapper;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.userCases.AuthUseCases;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthUseCases authUseCases;
  private final AuthControllerMapper userControllerMapper;

  public AuthController(AuthUseCases authUseCases, AuthControllerMapper userControllerMapper) {
    this.authUseCases = authUseCases;
    this.userControllerMapper = userControllerMapper;
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login(
      @Valid @RequestBody LoginRequestDto loginRequestDto) {

    TokenResponse login =
        authUseCases.login(userControllerMapper.loginRequestDtoToLoginCommand(loginRequestDto));

    // Crear cookie refresh
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh_token", login.getRefreshToken())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();

    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        .body(userControllerMapper.tokenResponseToTokenResponseDto(login));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDto> refresh(
      @CookieValue(name = "refresh_token") String refreshToken) {
    TokenResponse tokenDto = authUseCases.refreshToken(refreshToken);
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh_token", tokenDto.getRefreshToken())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();
    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        .body(userControllerMapper.tokenResponseToTokenResponseDto(tokenDto));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(name = "refresh_token", required = false) String refreshToken) {
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ZERO)
            .sameSite("Lax")
            .build();
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        .build();
  }
}
