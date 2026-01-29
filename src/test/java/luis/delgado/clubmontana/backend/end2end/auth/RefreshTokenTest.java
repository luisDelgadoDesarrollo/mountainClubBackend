package luis.delgado.clubmontana.backend.end2end.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;
import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.repository.AuthRepository;
import luis.delgado.clubmontana.backend.domain.services.TokenManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RefreshTokenTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TokenManagerService tokenManagerService;

  @Autowired private AuthRepository authRepository;

  // =====================================================
  // 1️⃣ HAPPY PATH
  // =====================================================
  @Test
  void refreshToken_happyPath_returnsNewTokens() throws Exception {

    Long userId = 1L;
    Long clubId = 10L;
    String email = "test@test.com";

    TokenResponse initial = tokenManagerService.generate(userId, clubId, email);

    String refreshToken = initial.getRefreshToken();

    mockMvc
        .perform(
            post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(refreshToken))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists())
        .andExpect(jsonPath("$.tokenType").value("Bearer"));
  }

  // =====================================================
  // 2️⃣ REFRESH TOKEN NO EXISTE
  // =====================================================
  @Test
  void refreshToken_notExists_returns401() throws Exception {

    String fakeRefreshToken = UUID.randomUUID().toString();

    mockMvc
        .perform(
            post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(fakeRefreshToken))
        .andExpect(status().isUnauthorized());
  }

  // =====================================================
  // 3️⃣ REFRESH TOKEN EXPIRADO
  // =====================================================
  @Test
  void refreshToken_expired_returns401() throws Exception {

    Long userId = 1L;
    Long clubId = 10L;
    String email = "test@test.com";

    String rawToken = UUID.randomUUID().toString();

    authRepository.saveTokenRefresh(
        new TokenRefresh(
            tokenManagerService.hashToken(rawToken),
            LocalDateTime.now().minusDays(1), // 👈 expirado
            userId,
            clubId,
            email));

    mockMvc
        .perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(rawToken))
        .andExpect(status().isUnauthorized());
  }

  // =====================================================
  // 4️⃣ REFRESH TOKEN INVALIDADO
  // =====================================================
  @Test
  void refreshToken_invalidated_returns401() throws Exception {

    Long userId = 1L;
    Long clubId = 10L;
    String email = "test@test.com";

    String rawToken = UUID.randomUUID().toString();

    TokenRefresh token =
        new TokenRefresh(
                tokenManagerService.hashToken(rawToken),
                LocalDateTime.now().plusDays(1),
                userId,
                clubId,
                email)
            .invalidate();

    authRepository.saveTokenRefresh(token);

    mockMvc
        .perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(rawToken))
        .andExpect(status().isUnauthorized());
  }

  // =====================================================
  // 5️⃣ REUTILIZAR EL MISMO REFRESH TOKEN (REPLAY ATTACK)
  // =====================================================
  @Test
  void refreshToken_usedTwice_secondTimeFails() throws Exception {

    Long userId = 1L;
    Long clubId = 10L;
    String email = "test@test.com";

    TokenResponse initial = tokenManagerService.generate(userId, clubId, email);

    String refreshToken = initial.getRefreshToken();

    // Primera vez → OK
    mockMvc
        .perform(
            post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(refreshToken))
        .andExpect(status().isCreated());

    // Segunda vez → FAIL
    mockMvc
        .perform(
            post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(refreshToken))
        .andExpect(status().isUnauthorized());
  }
}
