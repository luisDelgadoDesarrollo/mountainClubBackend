package luis.delgado.clubmontana.backend.application.services;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserCommand;
import luis.delgado.clubmontana.backend.domain.services.CreateUser;
import luis.delgado.clubmontana.backend.domain.services.PasswordManager;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UserEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UserEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.UserRepositoryMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateUserImpl implements CreateUser {

  private final UserEntityJpa userEntityJpa;
  private final UserRepositoryMapper userRepositoryMapper;
  private final PasswordManager passwordManager;

  public CreateUserImpl(
      UserEntityJpa userEntityJpa,
      UserRepositoryMapper userRepositoryMapper,
      PasswordManager passwordManager) {
    this.userEntityJpa = userEntityJpa;
    this.userRepositoryMapper = userRepositoryMapper;
    this.passwordManager = passwordManager;
  }

  @Override
  public User execute(CreateUserCommand user) {
    String password = passwordManager.hash(user.password());
    UserEntity userEntity = userRepositoryMapper.userToUserEntity(User.fromCommand(user));
    userEntity.setPassword(password);
    return userRepositoryMapper.userEntityToUser(userEntityJpa.save(userEntity));
  }
}
