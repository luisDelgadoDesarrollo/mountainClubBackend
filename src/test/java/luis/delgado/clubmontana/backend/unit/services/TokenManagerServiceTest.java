package luis.delgado.clubmontana.backend.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.crypto.SecretKey;
import luis.delgado.clubmontana.backend.application.services.TokenManagerServiceImpl;
import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenManagerServiceTest {

  private final long EXPIRATION_SECONDS = 900;
  @Mock private AuthRepository authRepository;
  private SecretKey secretKey;
  private TokenManagerServiceImpl tokenManagerService;

  @BeforeEach
  void setUp() {
    secretKey =
        io.jsonwebtoken.security.Keys.hmacShaKeyFor("01234567890123456789012345678901".getBytes());

    tokenManagerService =
        new TokenManagerServiceImpl(secretKey, EXPIRATION_SECONDS, authRepository);
  }

  @Test
  void generate_creates_access_and_refresh_token_and_persists_refresh_token() {
    // given
    Long userId = 1L;
    Long clubId = 10L;
    String email = "user@test.com";

    ArgumentCaptor<TokenRefresh> captor = ArgumentCaptor.forClass(TokenRefresh.class);

    // when
    TokenResponse response = tokenManagerService.generate(userId, clubId, email);

    // then
    assertNotNull(response);
    assertNotNull(response.getAccessToken());
    assertEquals("Bearer", response.getTokenType());
    assertEquals(EXPIRATION_SECONDS, response.getExpiresIn());
    assertNotNull(response.getRefreshToken());

    verify(authRepository).saveTokenRefresh(captor.capture());

    TokenRefresh savedToken = captor.getValue();

    assertEquals(userId, savedToken.userId());
    assertEquals(clubId, savedToken.clubId());
    assertEquals(email, savedToken.email());
    assertFalse(savedToken.invalid());

    // important: refresh token is NOT stored in clear
    assertNotEquals(response.getRefreshToken(), savedToken.token());
  }

  @Test
  void hashToken_is_deterministic_and_not_equal_to_raw() {
    // given
    String rawToken = "my-refresh-token";

    // when
    String hash1 = tokenManagerService.hashToken(rawToken);
    String hash2 = tokenManagerService.hashToken(rawToken);

    // then
    assertNotNull(hash1);
    assertEquals(hash1, hash2);
    assertNotEquals(rawToken, hash1);
  }

  @Test
  void matchHashToken_returns_true_when_tokens_match() {
    // given
    String rawToken = "token";
    String hash = tokenManagerService.hashToken(rawToken);

    // when
    boolean matches = tokenManagerService.matchHashToken(rawToken, hash);

    // then
    assertTrue(matches);
  }

  @Test
  void matchHashToken_returns_false_when_tokens_do_not_match() {
    // given
    String rawToken = "token1";
    String otherToken = "token2";
    String hash = tokenManagerService.hashToken(otherToken);

    // when
    boolean matches = tokenManagerService.matchHashToken(rawToken, hash);

    // then
    assertFalse(matches);
  }

  @Test
  void matchHashToken_returns_false_when_any_argument_is_null() {
    assertFalse(tokenManagerService.matchHashToken(null, "hash"));
    assertFalse(tokenManagerService.matchHashToken("token", null));
    assertFalse(tokenManagerService.matchHashToken(null, null));
  }
}
