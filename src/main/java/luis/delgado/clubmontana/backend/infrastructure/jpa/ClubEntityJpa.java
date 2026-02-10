package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubEntityJpa extends JpaRepository<ClubEntity, Long> {

    Optional<ClubEntity> findBySlug(String slug);
}
