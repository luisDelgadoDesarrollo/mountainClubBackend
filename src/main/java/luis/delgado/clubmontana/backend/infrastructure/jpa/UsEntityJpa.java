package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsEntityJpa extends JpaRepository<UsEntity, Long> {

  @Query(
      """
                  select u
                  from UsEntity u
                  left join fetch u.images
                  where u.club.clubId = :clubId
                """)
  Optional<UsEntity> findByClub_ClubId(Long clubId);
}
