package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Club;

public interface ClubRepository {
  Club getById(Long clubId);

  void save(Club club);
}
