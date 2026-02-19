package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubResponse;

public interface ClubUseCases {
  CreateClubResponse createClub(CreateClubRequest createClubRequest);

  Club getClub(Long clubId);

  void updateContact(Long clubId, String phone, String contactEmail);

  String getIban(Long clubId);
}
