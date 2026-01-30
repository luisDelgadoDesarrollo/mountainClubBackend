package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import luis.delgado.clubmontana.backend.api.exceptions.PublicationNotFoundException;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.PublicationEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.PublicationRepositoryMapper;
import org.springframework.stereotype.Repository;

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
}
