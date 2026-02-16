package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.ClubContactDto;
import luis.delgado.clubmontana.backend.api.dtos.ClubRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.ClubResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createClub.CreateClubResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubControllerMapper {
  CreateClubRequest clubRequestDtoToCreateCLubCommand(ClubRequestDto clubRequestDto);

  ClubResponseDto createCLubResponseToClubResponseDto(CreateClubResponse club);

  ClubContactDto clubToClubContactDto(Club club);
}
