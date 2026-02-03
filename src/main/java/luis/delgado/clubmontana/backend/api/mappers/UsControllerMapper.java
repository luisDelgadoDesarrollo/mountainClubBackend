package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.UsRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.UsResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Us;
import luis.delgado.clubmontana.backend.domain.model.UsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsControllerMapper {
  Us usRequestDroToUsRequest(UsRequestDto us);

  UsResponseDto useResponseToUsResponseDto(UsResponse usResponse);
}
