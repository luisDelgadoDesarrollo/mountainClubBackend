package luis.delgado.clubmontana.backend.domain.repository;

import java.util.Optional;
import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.UserToLogin;

public interface UserRepository {

  Optional<User> getUserByEmail(String email);

  Optional<UserToLogin> getUserByEmailToLogin(String email);

  User save(User user);
}
