package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.UserCreateRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.UserResponseDto;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {
  CreateUserRequest createUserDtoToCreateUserCommand(UserCreateRequestDto userCreateRequestDto);

  UserResponseDto userToUserResponseDto(CreateUserResponse createUser);
}
