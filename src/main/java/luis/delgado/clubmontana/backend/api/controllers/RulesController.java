package luis.delgado.clubmontana.backend.api.controllers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.RuleDto;
import luis.delgado.clubmontana.backend.api.mappers.RulesControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.userCases.RulesUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/{club}/rules")
public class RulesController {
  private final RulesUseCases rulesUseCases;
  private final RulesControllerMapper rulesControllerMapper;

  public RulesController(RulesUseCases rulesUseCases, RulesControllerMapper rulesControllerMapper) {
    this.rulesUseCases = rulesUseCases;
    this.rulesControllerMapper = rulesControllerMapper;
  }

  @PutMapping
  public ResponseEntity<Void> saveBylaws(@ClubId Long clubId, @RequestBody List<String> rules) {
    rulesUseCases.save(clubId, rules);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<List<RuleDto>> getUs(@ClubId Long clubId) {
    return ResponseEntity.ok()
        .body(
            rulesUseCases.get(clubId).stream().map(rulesControllerMapper::ruleToRuleDto).toList());
  }
}
