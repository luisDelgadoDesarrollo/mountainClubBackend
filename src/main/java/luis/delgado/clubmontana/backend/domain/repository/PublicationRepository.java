package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicationRepository {
  Publication savePublication(Publication publication);

  void deletePublication(Long publicationId, Long id);

  Publication getPublication(Long clubId, Long publicationId);

  Page<Publication> getPublications(Long clubId, Pageable pageable);

  Boolean existsBySlug(String s);
}
