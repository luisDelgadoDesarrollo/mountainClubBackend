package luis.delgado.clubmontana.backend.domain.services;

import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;

public interface TokenManagerService {
  TokenResponse generate(Long userId, Long clubId, String email);

  String hashToken(String token);

  Boolean matchHashToken(String rawToken, String hashToken);
}
