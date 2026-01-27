package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import java.util.Optional;
import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.repository.GetUserByEmailRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UserEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.UserRepositoryMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GetUserByEmailRepositoryImpl implements GetUserByEmailRepository {

  private final UserEntityJpa userEntityJpa;
  private final UserRepositoryMapper userRepositoryMapper;

  public GetUserByEmailRepositoryImpl(
      UserEntityJpa userEntityJpa, UserRepositoryMapper userRepositoryMapper) {
    this.userEntityJpa = userEntityJpa;
    this.userRepositoryMapper = userRepositoryMapper;
  }

  @Override
  public Optional<User> execute(String email) {
    return userEntityJpa.findByEmail(email).map(userRepositoryMapper::userEntityToUser);
  }
}
