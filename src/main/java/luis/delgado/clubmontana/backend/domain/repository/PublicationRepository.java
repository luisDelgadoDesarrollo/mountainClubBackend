package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Publication;

public interface PublicationRepository {
  Publication createPublication(Publication publication);
}
