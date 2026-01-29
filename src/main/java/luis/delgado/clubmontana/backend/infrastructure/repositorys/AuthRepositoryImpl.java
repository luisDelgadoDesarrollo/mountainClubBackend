package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.domain.repository.AuthRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.TokenRefreshEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.TokenRefreshRepositoryMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
  private final TokenRefreshEntityJpa tokenRefreshEntityJpa;
  private final TokenRefreshRepositoryMapper tokenRefreshRepositoryMapper;

  public AuthRepositoryImpl(
      TokenRefreshEntityJpa tokenRefreshEntityJpa,
      TokenRefreshRepositoryMapper tokenRefreshRepositoryMapper) {
    this.tokenRefreshEntityJpa = tokenRefreshEntityJpa;
    this.tokenRefreshRepositoryMapper = tokenRefreshRepositoryMapper;
  }

  @Override
  public TokenRefresh getRefreshToken(String tokenRefreshHashed) {
    return tokenRefreshEntityJpa
        .findByToken(tokenRefreshHashed)
        .map(tokenRefreshRepositoryMapper::tokenRefreshEntityToTOkenRefresh)
        .orElse(null);
  }

  @Override
  public void saveTokenRefresh(TokenRefresh tokenRefresh) {
    tokenRefreshEntityJpa.save(
        tokenRefreshRepositoryMapper.tokenRefreshToTokenRefreshEntity(tokenRefresh));
  }
}
