package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.ClubUserDto;
import luis.delgado.clubmontana.backend.api.dtos.CreateClubUserDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubUserControllerMapper {
  ClubUser createClubUserDtoToCreateClubUser(CreateClubUserDto createClubUserDto);

  ResponseDto createClubToResponseDto(ClubUser clubUser);

  ClubUserDto clubUserToClubUserDto(ClubUser clubUser);
}
