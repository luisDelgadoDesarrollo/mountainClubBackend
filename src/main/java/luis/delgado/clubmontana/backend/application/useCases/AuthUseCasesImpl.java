package luis.delgado.clubmontana.backend.application.useCases;

import java.time.LocalDateTime;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.LoginRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.repository.AuthRepository;
import luis.delgado.clubmontana.backend.domain.repository.UserRepository;
import luis.delgado.clubmontana.backend.domain.services.PasswordManagerService;
import luis.delgado.clubmontana.backend.domain.services.TokenManagerService;
import luis.delgado.clubmontana.backend.domain.userCases.AuthUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@UseCase
public class AuthUseCasesImpl implements AuthUseCases {
  private final TokenManagerService tokenManagerService;
  private final AuthRepository authRepository;
  private final UserRepository userRepository;
  private final PasswordManagerService passwordManagerService;

  public AuthUseCasesImpl(
      TokenManagerService tokenManagerService,
      AuthRepository authRepository,
      UserRepository userRepository,
      PasswordManagerService passwordManagerService) {
    this.tokenManagerService = tokenManagerService;
    this.authRepository = authRepository;
    this.userRepository = userRepository;
    this.passwordManagerService = passwordManagerService;
  }

  @Override
  public TokenResponse login(LoginRequest loginCommand) {
    return userRepository
        .getUserByEmailToLogin(loginCommand.getUsername())
        .filter(
            user ->
                passwordManagerService.matches(loginCommand.getPassword(), user.getPassword())
                    && user.getEmailVerified())
        .map(
            userToLogin ->
                tokenManagerService.generate(
                    userToLogin.getUserId(), userToLogin.getClubId(), userToLogin.getEmail()))
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
  }

  @Override
  public TokenResponse refreshToken(String refreshToken) {

    TokenRefresh tokenRefresh =
        authRepository.getRefreshToken(tokenManagerService.hashToken(refreshToken));
    if (tokenRefresh == null
        || tokenRefresh.invalid()
        || tokenRefresh.expiresAt().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Refresh invalido");
    }
    authRepository.saveTokenRefresh(tokenRefresh.invalidate());
    return tokenManagerService.generate(
        tokenRefresh.clubId(), tokenRefresh.userId(), tokenRefresh.email());
  }
}
