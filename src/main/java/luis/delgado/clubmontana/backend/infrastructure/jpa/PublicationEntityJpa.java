package luis.delgado.clubmontana.backend.infrastructure.jpa;

import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationEntityJpa extends JpaRepository<PublicationEntity, Long> {}
