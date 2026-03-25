package luis.delgado.clubmontana.backend.domain.useCases;

import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserResponse;

public interface UserUseCases {

  public CreateUserResponse createUserUseCase(CreateUserRequest createUserRequest);
}
