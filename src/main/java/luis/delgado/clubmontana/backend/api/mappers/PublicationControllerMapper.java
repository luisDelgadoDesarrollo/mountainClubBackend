package luis.delgado.clubmontana.backend.api.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationIdResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.mapstruct.Mapper;
import org.springframework.data.util.Pair;

@Mapper(componentModel = "spring")
public interface PublicationControllerMapper {

  default List<PublicationResponseDto> publicationsWithPathToPublicationResponseDtoList(
      List<Pair<Publication, List<String>>> publicationsWithPath) {
    return publicationsWithPath.stream()
        .map(pair -> publicationResponseToPublicationResponseDto(pair.getFirst(), pair.getSecond()))
        .toList();
  }

  PublicationIdResponseDto publicationToPublicationResponseDto(Publication publication);

  Publication publicationRequestDtoToCreatePublicationCommand(
      CreatePublicationRequestDto createPublicationRequestDto);

  PublicationResponseDto publicationResponseToPublicationResponseDto(
      Publication publication, List<String> imagesPath);
}
