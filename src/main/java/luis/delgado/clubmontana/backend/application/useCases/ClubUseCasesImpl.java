package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubCommand;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubResponse;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUseCases;
import org.springframework.stereotype.Component;

@Component
public class ClubUseCasesImpl implements ClubUseCases {
  @Override
  public CreateClubResponse createClub(CreateClubCommand createClubCommand) {
    return null;
  }
}
