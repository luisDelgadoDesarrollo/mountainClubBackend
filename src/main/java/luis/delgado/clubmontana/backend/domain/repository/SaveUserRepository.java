package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.User;

public interface SaveUserRepository {
  User execute(User user);
}
