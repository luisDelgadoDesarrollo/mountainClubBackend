package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.commands.createPublication.CreatePublicationRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createPublication.CreatePublicationResponse;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;

@UseCase
public class PublicationUseCasesImpl implements PublicationUseCases {
  @Override
  public CreatePublicationResponse create(
      Long clubId, CreatePublicationRequest createPublicationRequest) {

    return null;
  }
}
