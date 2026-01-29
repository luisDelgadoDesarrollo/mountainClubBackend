package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.domain.model.commands.createPublication.CreatePublicationRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createPublication.CreatePublicationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicationControllerMapper {

  PublicationResponseDto createPublicationResponseToPublicationResponseDto(
      CreatePublicationResponse createPublicationResponse);

  CreatePublicationRequest publicationRequestDtoToCreatePublicationCommand(
      CreatePublicationRequestDto createPublicationRequestDto);
}
