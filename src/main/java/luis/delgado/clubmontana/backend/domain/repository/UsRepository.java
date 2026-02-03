package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Us;

public interface UsRepository {

  Us save(Us us, Long clubId);

  Us update(Us us, Long clubId);

  Us get(Long clubId);
}
