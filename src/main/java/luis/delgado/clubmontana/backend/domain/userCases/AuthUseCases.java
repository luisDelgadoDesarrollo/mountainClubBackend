package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.commands.auth.LoginRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;

public interface AuthUseCases {
  public TokenResponse login(LoginRequest loginCommand);

  TokenResponse refreshToken(String refreshToken);
}
