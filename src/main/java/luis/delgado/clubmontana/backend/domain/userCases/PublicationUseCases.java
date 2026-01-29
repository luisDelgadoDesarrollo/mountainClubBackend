package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.commands.createPublication.CreatePublicationRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createPublication.CreatePublicationResponse;

public interface PublicationUseCases {
  CreatePublicationResponse create(Long clubId, CreatePublicationRequest createPublicationRequest);
}
