package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Publication;

public interface PublicationRepository {
  Publication savePublication(Publication publication);

  void deletePublication(Long publicationId, Long id);

  Publication getPublication(Long clubId, Long publicationId);
}
