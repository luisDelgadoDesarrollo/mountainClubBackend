package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Rules;

public interface RulesUseCases {

  void save(Long clubId, List<String> rules);

  List<Rules> get(Long clubId);
}
