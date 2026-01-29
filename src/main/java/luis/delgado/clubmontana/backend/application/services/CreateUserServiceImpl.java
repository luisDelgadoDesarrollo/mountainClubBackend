package luis.delgado.clubmontana.backend.application.services;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserRequest;
import luis.delgado.clubmontana.backend.domain.services.CreateUserService;
import luis.delgado.clubmontana.backend.domain.services.PasswordManagerService;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UserEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UserEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.UserRepositoryMapper;
import org.springframework.stereotype.Service;

@Service
public class CreateUserServiceImpl implements CreateUserService {

  private final UserEntityJpa userEntityJpa;
  private final UserRepositoryMapper userRepositoryMapper;
  private final PasswordManagerService passwordManagerService;

  public CreateUserServiceImpl(
      UserEntityJpa userEntityJpa,
      UserRepositoryMapper userRepositoryMapper,
      PasswordManagerService passwordManagerService) {
    this.userEntityJpa = userEntityJpa;
    this.userRepositoryMapper = userRepositoryMapper;
    this.passwordManagerService = passwordManagerService;
  }

  @Override
  public User execute(CreateUserRequest user) {
    String password = passwordManagerService.hash(user.getPassword());
    UserEntity userEntity = userRepositoryMapper.userToUserEntity(User.fromCommand(user));
    userEntity.setPassword(password);
    return userRepositoryMapper.userEntityToUser(userEntityJpa.save(userEntity));
  }
}
