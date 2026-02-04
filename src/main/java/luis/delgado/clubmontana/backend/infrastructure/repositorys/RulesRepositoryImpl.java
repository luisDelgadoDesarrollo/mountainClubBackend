package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Rules;
import luis.delgado.clubmontana.backend.domain.repository.RulesRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.RulesEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.RulesRepositoryMapper;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class RulesRepositoryImpl implements RulesRepository {

  private final RulesRepositoryMapper rulesRepositoryMapper;
  private final RulesEntityJpa rulesEntityJpa;

  public RulesRepositoryImpl(
      RulesRepositoryMapper rulesRepositoryMapper, RulesEntityJpa rulesEntityJpa) {
    this.rulesRepositoryMapper = rulesRepositoryMapper;
    this.rulesEntityJpa = rulesEntityJpa;
  }

  @Override
  public void delete(Long clubId) {
    rulesEntityJpa.deleteByClub_clubId(clubId);
  }

  @Override
  public void save(List<Rules> rules) {
    rulesEntityJpa.saveAll(rules.stream().map(rulesRepositoryMapper::rulesToRulesEntity).toList());
  }

  @Override
  public List<Rules> get(Long clubId) {
    return rulesEntityJpa.findAllByClub_clubId(clubId).stream()
        .map(rulesRepositoryMapper::rulesEntityToRules)
        .toList();
  }
}
