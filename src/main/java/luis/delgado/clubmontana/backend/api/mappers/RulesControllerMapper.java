package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.RuleDto;
import luis.delgado.clubmontana.backend.domain.model.Rules;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RulesControllerMapper {
  RuleDto ruleToRuleDto(Rules rules);
}
