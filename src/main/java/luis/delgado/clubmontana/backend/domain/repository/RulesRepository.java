package luis.delgado.clubmontana.backend.domain.repository;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Rules;

public interface RulesRepository {

  void delete(Long clubId);

  void save(List<Rules> rules);

  List<Rules> get(Long clubId);
}
