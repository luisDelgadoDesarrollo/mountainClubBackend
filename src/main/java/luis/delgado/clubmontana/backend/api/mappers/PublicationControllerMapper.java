package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicationControllerMapper {

  PublicationResponseDto publicationToPublicationResponseDto(Publication publication);

  Publication publicationRequestDtoToCreatePublicationCommand(
      CreatePublicationRequestDto createPublicationRequestDto);
}
