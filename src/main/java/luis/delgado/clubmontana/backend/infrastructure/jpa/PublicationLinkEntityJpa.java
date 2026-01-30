package luis.delgado.clubmontana.backend.infrastructure.jpa;

import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationLinkEntityJpa extends JpaRepository<PublicationLinkEntity, Long> {}
