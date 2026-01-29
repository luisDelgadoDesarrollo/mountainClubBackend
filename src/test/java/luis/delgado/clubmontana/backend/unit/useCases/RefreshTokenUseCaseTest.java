package luis.delgado.clubmontana.backend.unit.useCases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import luis.delgado.clubmontana.backend.application.useCases.AuthUseCasesImpl;
import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.repository.AuthRepository;
import luis.delgado.clubmontana.backend.domain.services.TokenManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

  private final String RAW_REFRESH_TOKEN = "refresh-token";
  @Mock private TokenManagerService tokenManagerService;
  @Mock private AuthRepository authRepository;
  @InjectMocks private AuthUseCasesImpl authUseCases;

  @Test
  void refreshToken_success_when_token_is_valid() {
    // given
    TokenRefresh tokenRefresh =
        new TokenRefresh(
            1L,
            RAW_REFRESH_TOKEN,
            LocalDateTime.now().plusDays(1),
            false,
            10L,
            20L,
            "user@test.com");

    TokenResponse tokenResponse =
        new TokenResponse("new-access-token", "Bearer", 900L, RAW_REFRESH_TOKEN);

    when(authRepository.getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN)))
        .thenReturn(tokenRefresh);

    when(tokenManagerService.generate(
            tokenRefresh.clubId(), tokenRefresh.userId(), tokenRefresh.email()))
        .thenReturn(tokenResponse);

    // when
    TokenResponse result = authUseCases.refreshToken(RAW_REFRESH_TOKEN);

    // then
    assertNotNull(result);
    assertEquals(tokenResponse, result);

    verify(authRepository).getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN));
    verify(authRepository).saveTokenRefresh(tokenRefresh.invalidate());
    verify(tokenManagerService)
        .generate(tokenRefresh.clubId(), tokenRefresh.userId(), tokenRefresh.email());
  }

  @Test
  void refreshToken_fails_when_token_does_not_exist() {
    // given
    when(authRepository.getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN)))
        .thenReturn(null);

    // when
    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class, () -> authUseCases.refreshToken(RAW_REFRESH_TOKEN));

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    verify(authRepository).getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN));
  }

  @Test
  void refreshToken_fails_when_token_is_invalidated() {
    // given
    TokenRefresh tokenRefresh =
        new TokenRefresh(
            1L,
            RAW_REFRESH_TOKEN,
            LocalDateTime.now().plusDays(1),
            true,
            10L,
            20L,
            "user@test.com");

    when(authRepository.getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN)))
        .thenReturn(tokenRefresh);

    // when
    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class, () -> authUseCases.refreshToken(RAW_REFRESH_TOKEN));

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    verify(authRepository).getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN));
  }

  @Test
  void refreshToken_fails_when_token_is_expired() {
    // given
    TokenRefresh tokenRefresh =
        new TokenRefresh(
            1L,
            RAW_REFRESH_TOKEN,
            LocalDateTime.now().minusMinutes(1),
            false,
            10L,
            20L,
            "user@test.com");

    when(authRepository.getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN)))
        .thenReturn(tokenRefresh);

    // when
    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class, () -> authUseCases.refreshToken(RAW_REFRESH_TOKEN));

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    verify(authRepository).getRefreshToken(tokenManagerService.hashToken(RAW_REFRESH_TOKEN));
  }
}
