package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import luis.delgado.clubmontana.backend.api.dtos.UserCreateRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.UserResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.UserControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.UserUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserUseCases userUseCases;
  private final UserControllerMapper userControllerMapper;

  public UserController(UserUseCases userUseCases, UserControllerMapper userControllerMapper) {
    this.userUseCases = userUseCases;
    this.userControllerMapper = userControllerMapper;
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(
      @Valid @RequestBody UserCreateRequestDto userDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            userControllerMapper.userToUserResponseDto(
                userUseCases.createUserUseCase(
                    userControllerMapper.createUserDtoToCreateUserCommand(userDto))));
  }
}
