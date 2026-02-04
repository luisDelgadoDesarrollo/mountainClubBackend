package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.Rules;
import luis.delgado.clubmontana.backend.infrastructure.entitys.RulesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RulesRepositoryMapper {

  @Mapping(target = "clubId", source = "club.clubId")
  Rules rulesEntityToRules(RulesEntity rulesEntity);

  @Mapping(target = "club.clubId", source = "clubId")
  RulesEntity rulesToRulesEntity(Rules rules);
}
