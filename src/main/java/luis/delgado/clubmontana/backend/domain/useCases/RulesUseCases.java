package luis.delgado.clubmontana.backend.domain.useCases;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Rules;

public interface RulesUseCases {

  void save(Long clubId, List<String> rules);

  List<Rules> get(Long clubId);
}
