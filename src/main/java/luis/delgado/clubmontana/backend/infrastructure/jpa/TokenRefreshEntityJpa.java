package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.TokenRefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRefreshEntityJpa extends JpaRepository<TokenRefreshEntity, Long> {
  Optional<TokenRefreshEntity> findByToken(String token);
}
