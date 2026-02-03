package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.Us;
import luis.delgado.clubmontana.backend.domain.model.UsImage;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsRepositoryMapper {

  Us usEntityToUs(UsEntity usEntity);

  UsEntity usToUsEntity(Us us);

  @Mapping(target = "us.clubId", source = "clubId")
  UsImageEntity usImageToUsImageEntity(UsImage usImage);
}
