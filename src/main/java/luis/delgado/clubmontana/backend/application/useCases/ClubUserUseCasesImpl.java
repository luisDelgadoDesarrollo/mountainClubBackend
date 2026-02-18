package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import luis.delgado.clubmontana.backend.domain.repository.ClubUserRepository;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUserUseCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
public class ClubUserUseCasesImpl implements ClubUserUseCases {

  private final ClubUserRepository clubUserRepository;

  public ClubUserUseCasesImpl(ClubUserRepository clubUserRepository) {
    this.clubUserRepository = clubUserRepository;
  }

  @Override
  public ClubUser create(Long clubId, ClubUser clubUser) {
    clubUser.setClubId(clubId);
    return clubUserRepository.save(clubUser);
  }

  @NoAuthenticationNeeded
  @Override
  public ClubUser get(Long clubId, String email) {
    return clubUserRepository.getUserByClubAndEmail(clubId, email);
  }

  @Override
  public ClubUser update(Long clubId, String email, ClubUser clubUser) {
    clubUserRepository.getUserByClubAndEmail(clubId, email);
    clubUser.setClubId(clubId);
    clubUser.setEmail(email);
    return clubUserRepository.save(clubUser);
  }

  @Override
  public void delete(Long clubId, String email) {
    clubUserRepository.delete(clubId, email);
  }

  @NoAuthenticationNeeded
  @Override
  public Page<ClubUser> getAll(Long clubId, Pageable pageable) {
    return clubUserRepository.getAll(clubId, pageable);
  }
}
