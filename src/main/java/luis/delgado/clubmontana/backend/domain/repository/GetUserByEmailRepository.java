package luis.delgado.clubmontana.backend.domain.repository;

import java.util.Optional;
import luis.delgado.clubmontana.backend.domain.model.User;

public interface GetUserByEmailRepository {
  Optional<User> execute(String email);
}
