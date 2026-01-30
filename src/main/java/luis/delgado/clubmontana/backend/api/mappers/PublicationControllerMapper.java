package luis.delgado.clubmontana.backend.api.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationIdResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicationControllerMapper {

  PublicationIdResponseDto publicationToPublicationResponseDto(Publication publication);

  Publication publicationRequestDtoToCreatePublicationCommand(
      CreatePublicationRequestDto createPublicationRequestDto);

  PublicationResponseDto publicationResponseToPublicationResponseDto(
      Publication publication, List<String> imagesPath);
}
