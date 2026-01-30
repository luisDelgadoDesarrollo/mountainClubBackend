package luis.delgado.clubmontana.backend.infrastructure.jpa;

import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationImageEntityJpa extends JpaRepository<PublicationImageEntity, Long> {}
