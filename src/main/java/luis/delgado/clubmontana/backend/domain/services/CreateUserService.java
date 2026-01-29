package luis.delgado.clubmontana.backend.domain.services;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserRequest;

public interface CreateUserService {
  User execute(CreateUserRequest user);
}
