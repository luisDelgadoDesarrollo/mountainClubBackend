package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityJpa extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);
}
