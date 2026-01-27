package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.repository.SaveUserRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UserEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.UserRepositoryMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SaveUserRepositoryImpl implements SaveUserRepository {

  private final UserEntityJpa userEntityJpa;
  private final UserRepositoryMapper userRepositoryMapper;

  public SaveUserRepositoryImpl(
      UserEntityJpa userEntityJpa, UserRepositoryMapper userRepositoryMapper) {
    this.userEntityJpa = userEntityJpa;
    this.userRepositoryMapper = userRepositoryMapper;
  }

  @Override
  public User execute(User user) {
    return userRepositoryMapper.userEntityToUser(
        userEntityJpa.save(userRepositoryMapper.userToUserEntity(user)));
  }
}
