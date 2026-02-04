package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.List;
import luis.delgado.clubmontana.backend.infrastructure.entitys.RulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface RulesEntityJpa extends JpaRepository<RulesEntity, Long> {
  List<RulesEntity> findAllByClub_clubId(Long clubId);

  @Modifying
  void deleteByClub_clubId(Long clubId);
}
