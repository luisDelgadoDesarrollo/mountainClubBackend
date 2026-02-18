package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubUserRepositoryMapper {
  ClubUserEntity clubUserToClubUserEntity(ClubUser clubUser);

  ClubUser clubUserEntityToClubUser(ClubUserEntity clubUserEntity);
}
