package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.LoginRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.TokenResponseDto;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.LoginRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthControllerMapper {

  LoginRequest loginRequestDtoToLoginCommand(LoginRequestDto loginRequestDto);

  TokenResponseDto tokenResponseToTokenResponseDto(TokenResponse tokenDto);
}
