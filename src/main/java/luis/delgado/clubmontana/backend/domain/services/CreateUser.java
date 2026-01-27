package luis.delgado.clubmontana.backend.domain.services;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserCommand;

public interface CreateUser {
  User execute(CreateUserCommand user);
}
