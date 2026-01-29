package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.UserToLogin;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRepositoryMapper {

  @Mapping(target = "clubId", source = "club.clubId")
  User userEntityToUser(UserEntity user);

  @Mapping(target = "club.clubId", source = "clubId")
  UserEntity userToUserEntity(User user);

  @Mapping(target = "clubId", source = "club.clubId")
  UserToLogin userEntityToUserToLogin(UserEntity userEntity);
}
