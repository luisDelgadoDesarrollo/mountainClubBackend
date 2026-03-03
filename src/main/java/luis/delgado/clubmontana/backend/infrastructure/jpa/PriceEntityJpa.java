package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.List;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface PriceEntityJpa extends JpaRepository<PriceEntity, Long> {
  List<PriceEntity> findByClub_ClubId(Long clubId);

  @Modifying
  void deleteByClub_ClubId(Long clubId);
}
