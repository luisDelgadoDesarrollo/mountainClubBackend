package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubRepositoryMapper {
  Club clubEntityToClub(ClubEntity clubEntity);

  ClubEntity clubToClubEntity(Club club);
}
