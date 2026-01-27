package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserCommand;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserResponse;

public interface UserUseCases {

  public CreateUserResponse createUserUseCase(CreateUserCommand createUserCommand);
}
