package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import java.util.Optional;
import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.UserToLogin;
import luis.delgado.clubmontana.backend.domain.repository.UserRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UserEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.UserRepositoryMapper;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class UserRepositoryImpl implements UserRepository {

  private final UserEntityJpa userEntityJpa;
  private final UserRepositoryMapper userRepositoryMapper;

  public UserRepositoryImpl(
      UserEntityJpa userEntityJpa, UserRepositoryMapper userRepositoryMapper) {
    this.userEntityJpa = userEntityJpa;
    this.userRepositoryMapper = userRepositoryMapper;
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    return userEntityJpa.findByEmail(email).map(userRepositoryMapper::userEntityToUser);
  }

  @Override
  public Optional<UserToLogin> getUserByEmailToLogin(String email) {
    return userEntityJpa.findByEmail(email).map(userRepositoryMapper::userEntityToUserToLogin);
  }

  @Override
  public User save(User user) {
    return userRepositoryMapper.userEntityToUser(
        userEntityJpa.save(userRepositoryMapper.userToUserEntity(user)));
  }
}
