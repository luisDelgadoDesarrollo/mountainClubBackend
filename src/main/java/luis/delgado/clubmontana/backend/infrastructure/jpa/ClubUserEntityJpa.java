package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubUserEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ClubUserIdEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubUserEntityJpa extends JpaRepository<ClubUserEntity, ClubUserIdEntity> {
  Optional<ClubUserEntity> findByClubIdAndEmail(Long clubId, String email);

  void deleteByClubIdAndEmail(Long clubId, String email);

  Page<ClubUserEntity> findAllByClubId(Long clubId, Pageable pageable);
}
