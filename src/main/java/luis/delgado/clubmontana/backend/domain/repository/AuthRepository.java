package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;

public interface AuthRepository {

  TokenRefresh getRefreshToken(String tokenRefresh);

  void saveTokenRefresh(TokenRefresh tokenRefresh);
}
