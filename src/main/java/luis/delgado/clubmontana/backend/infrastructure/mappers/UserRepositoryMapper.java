package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRepositoryMapper {
  User userEntityToUser(UserEntity user);

  UserEntity userToUserEntity(User user);
}
