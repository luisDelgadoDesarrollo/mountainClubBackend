package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.UsRequestDto;
import luis.delgado.clubmontana.backend.domain.model.UsRequest;
import luis.delgado.clubmontana.backend.domain.model.UsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsControllerMapper {
  UsRequest usRequestDroToUsRequest(UsRequestDto us);

  PublicationResponseDto useResponseToUsResponseDto(UsResponse usResponse);
}
