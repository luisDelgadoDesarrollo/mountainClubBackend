package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubCommand;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubResponse;

public interface ClubUseCases {
  CreateClubResponse createClub(CreateClubCommand createClubCommand);
}
