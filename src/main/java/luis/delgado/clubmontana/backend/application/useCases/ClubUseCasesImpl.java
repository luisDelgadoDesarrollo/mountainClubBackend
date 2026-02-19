package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubResponse;
import luis.delgado.clubmontana.backend.domain.repository.ClubRepository;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUseCases;

@UseCase
public class ClubUseCasesImpl implements ClubUseCases {

  private final ClubRepository clubRepository;

  public ClubUseCasesImpl(ClubRepository clubRepository) {
    this.clubRepository = clubRepository;
  }

  @Override
  public CreateClubResponse createClub(CreateClubRequest createClubRequest) {
    return null;
  }

  @NoAuthenticationNeeded
  @Override
  public Club getClub(Long clubId) {
    return clubRepository.getById(clubId);
  }

  @Override
  public void updateContact(Long clubId, String phone, String contactEmail) {
    Club club = clubRepository.getById(clubId);
    club.setContactEmail(contactEmail);
    club.setPhone(phone);
    clubRepository.save(club);
  }

  @NoAuthenticationNeeded
  @Override
  public String getIban(Long clubId) {
    return clubRepository.getById(clubId).getIban();
  }
}
