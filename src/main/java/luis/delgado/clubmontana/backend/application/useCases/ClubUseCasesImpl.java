package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubResponse;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUseCases;

@UseCase
public class ClubUseCasesImpl implements ClubUseCases {
  @Override
  public CreateClubResponse createClub(CreateClubRequest createClubRequest) {
    return null;
  }
}
