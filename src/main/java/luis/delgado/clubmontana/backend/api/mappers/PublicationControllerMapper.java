package luis.delgado.clubmontana.backend.api.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicationControllerMapper {


  @Mapping(target = "id", source = "publicationId")
  ResponseDto publicationToPublicationResponseDto(Publication publication);

  Publication publicationRequestDtoToCreatePublicationCommand(
      CreatePublicationRequestDto createPublicationRequestDto);

  PublicationResponseDto publicationResponseToPublicationResponseDto(
      Publication publication, List<String> imagesPath);
}
