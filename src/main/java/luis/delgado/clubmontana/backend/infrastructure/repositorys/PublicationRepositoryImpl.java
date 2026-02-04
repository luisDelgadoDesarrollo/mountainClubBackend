package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.api.exceptions.PublicationNotFoundException;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.PublicationEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.PublicationRepositoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class PublicationRepositoryImpl implements PublicationRepository {

  private final PublicationEntityJpa publicationEntityJpa;
  private final PublicationRepositoryMapper publicationRepositoryMapper;

  public PublicationRepositoryImpl(
      PublicationEntityJpa publicationEntityJpa,
      PublicationRepositoryMapper publicationRepositoryMapper) {
    this.publicationEntityJpa = publicationEntityJpa;
    this.publicationRepositoryMapper = publicationRepositoryMapper;
  }

  @Override
  public Publication savePublication(Publication publication) {
    return publicationRepositoryMapper.publicationEntityToPublication(
        publicationEntityJpa.save(
            publicationRepositoryMapper.publicationToPublicationEntity(publication)));
  }

  @Override
  public void deletePublication(Long clubId, Long publicationId) {
    publicationEntityJpa.deleteByClub_ClubIdAndPublicationId(clubId, publicationId);
  }

  @Override
  public Publication getPublication(Long clubId, Long publicationId) {
    return publicationRepositoryMapper.publicationEntityToPublication(
        publicationEntityJpa
            .findByClubAndId(clubId, publicationId)
            .orElseThrow(() -> new PublicationNotFoundException(clubId, publicationId)));
  }

  @Override
  public Page<Publication> getPublications(Long clubId, Pageable pageable) {
    return publicationEntityJpa
        .findByClub_ClubId(clubId, pageable)
        .map(publicationRepositoryMapper::publicationEntityToPublication);
  }
}
