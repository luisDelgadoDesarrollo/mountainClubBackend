package luis.delgado.clubmontana.backend.unit.useCases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import luis.delgado.clubmontana.backend.application.useCases.AuthUseCasesImpl;
import luis.delgado.clubmontana.backend.domain.model.UserToLogin;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.LoginRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.repository.UserRepository;
import luis.delgado.clubmontana.backend.domain.services.PasswordManagerService;
import luis.delgado.clubmontana.backend.domain.services.TokenManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordManagerService passwordManagerService;

  @Mock private TokenManagerService tokenManagerService;

  @InjectMocks private AuthUseCasesImpl authUseCases;

  private LoginRequest loginRequest;
  private UserToLogin user;

  @BeforeEach
  void setUp() {
    loginRequest = new LoginRequest("user@test.com", "password123");

    user =
        UserToLogin.builder()
            .userId(1L)
            .clubId(10L)
            .email("user@test.com")
            .password("hashedPassword")
            .emailVerified(true)
            .build();
  }

  @Test
  void login_success_when_credentials_are_valid() {
    // given
    TokenResponse tokenResponse =
        new TokenResponse("access-token", "Bearer", 900L, "refresh-token");

    when(userRepository.getUserByEmailToLogin(loginRequest.getUsername()))
        .thenReturn(Optional.of(user));

    when(passwordManagerService.matches(loginRequest.getPassword(), user.getPassword()))
        .thenReturn(true);

    when(tokenManagerService.generate(user.getUserId(), user.getClubId(), user.getEmail()))
        .thenReturn(tokenResponse);

    // when
    TokenResponse result = authUseCases.login(loginRequest);

    // then
    assertNotNull(result);
    assertEquals(tokenResponse, result);

    verify(userRepository).getUserByEmailToLogin(loginRequest.getUsername());
    verify(passwordManagerService).matches(loginRequest.getPassword(), user.getPassword());
    verify(tokenManagerService).generate(user.getUserId(), user.getClubId(), user.getEmail());
  }

  @Test
  void login_fails_when_user_does_not_exist() {
    // given
    when(userRepository.getUserByEmailToLogin(loginRequest.getUsername()))
        .thenReturn(Optional.empty());

    // when
    ResponseStatusException exception =
        assertThrows(ResponseStatusException.class, () -> authUseCases.login(loginRequest));

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    assertEquals("Credenciales inválidas", exception.getReason());

    verify(userRepository).getUserByEmailToLogin(loginRequest.getUsername());
    verifyNoInteractions(passwordManagerService, tokenManagerService);
  }

  @Test
  void login_fails_when_password_is_incorrect() {
    // given
    when(userRepository.getUserByEmailToLogin(loginRequest.getUsername()))
        .thenReturn(Optional.of(user));

    when(passwordManagerService.matches(loginRequest.getPassword(), user.getPassword()))
        .thenReturn(false);

    // when
    ResponseStatusException exception =
        assertThrows(ResponseStatusException.class, () -> authUseCases.login(loginRequest));

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    verify(userRepository).getUserByEmailToLogin(loginRequest.getUsername());
    verify(passwordManagerService).matches(loginRequest.getPassword(), user.getPassword());
    verifyNoInteractions(tokenManagerService);
  }

  @Test
  void login_fails_when_email_is_not_verified() {
    // given
    user =
        UserToLogin.builder()
            .userId(1L)
            .clubId(10L)
            .email("user@test.com")
            .password("hashedPassword")
            .emailVerified(false)
            .build();

    when(userRepository.getUserByEmailToLogin(loginRequest.getUsername()))
        .thenReturn(Optional.of(user));

    when(passwordManagerService.matches(loginRequest.getPassword(), user.getPassword()))
        .thenReturn(true);

    // when
    ResponseStatusException exception =
        assertThrows(ResponseStatusException.class, () -> authUseCases.login(loginRequest));

    // then
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    verify(userRepository).getUserByEmailToLogin(loginRequest.getUsername());
    verify(passwordManagerService).matches(loginRequest.getPassword(), user.getPassword());
    verifyNoInteractions(tokenManagerService);
  }
}
