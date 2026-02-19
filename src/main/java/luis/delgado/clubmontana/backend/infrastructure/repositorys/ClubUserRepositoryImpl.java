package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.api.exceptions.ClubUserNotFoundException;
import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import luis.delgado.clubmontana.backend.domain.repository.ClubUserRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubUserEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.ClubUserRepositoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ClubUserRepositoryImpl implements ClubUserRepository {

  private final ClubUserEntityJpa clubUserEntityJpa;
  private final ClubUserRepositoryMapper clubUserRepositoryMapper;

  public ClubUserRepositoryImpl(
      ClubUserEntityJpa clubUserEntityJpa, ClubUserRepositoryMapper clubUserRepositoryMapper) {
    this.clubUserEntityJpa = clubUserEntityJpa;
    this.clubUserRepositoryMapper = clubUserRepositoryMapper;
  }

  @Override
  public ClubUser save(ClubUser clubUser) {
    return clubUserRepositoryMapper.clubUserEntityToClubUser(
        clubUserEntityJpa.save(clubUserRepositoryMapper.clubUserToClubUserEntity(clubUser)));
  }

  @Override
  public ClubUser getUserByClubAndEmail(Long clubId, String email) {
    return clubUserEntityJpa
        .findByClubIdAndEmail(clubId, email)
        .map(clubUserRepositoryMapper::clubUserEntityToClubUser)
        .orElseThrow(() -> new ClubUserNotFoundException(clubId, email));
  }

  @Override
  public void delete(Long clubId, String email) {
    clubUserEntityJpa.deleteByClubIdAndEmail(clubId, email);
  }

  @Override
  public Page<ClubUser> getAll(Long clubId, Pageable pageable) {
    return clubUserEntityJpa
        .findAllByClubId(clubId, pageable)
        .map(clubUserRepositoryMapper::clubUserEntityToClubUser);
  }

  @Override
  public ClubUser getUserByClubAndEmailAndNif(Long clubId, String email, String nif) {
    return clubUserEntityJpa
        .getUserByClubIdAndEmailAndNif(clubId, email, nif)
        .map(clubUserRepositoryMapper::clubUserEntityToClubUser)
        .orElse(null);
  }
}
