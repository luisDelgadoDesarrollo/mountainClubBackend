package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Rules;
import luis.delgado.clubmontana.backend.domain.repository.RulesRepository;
import luis.delgado.clubmontana.backend.domain.userCases.RulesUseCases;

@UseCase
public class RulesUseCasesImpl implements RulesUseCases {

  private final RulesRepository rulesRepository;

  public RulesUseCasesImpl(RulesRepository rulesRepository) {
    this.rulesRepository = rulesRepository;
  }

  @Override
  public void save(Long clubId, List<String> rules) {
    rulesRepository.delete(clubId);
    if (rules.isEmpty()) return;
    rulesRepository.save(
        rules.stream().map(rule -> Rules.builder().rule(rule).clubId(clubId).build()).toList());
  }

  @NoAuthenticationNeeded
  @Override
  public List<Rules> get(Long clubId) {
    AtomicLong counter = new AtomicLong(1L);
    return rulesRepository.get(clubId).stream()
        .peek(rule -> rule.setRuleId(counter.getAndIncrement()))
        .toList();
  }
}
