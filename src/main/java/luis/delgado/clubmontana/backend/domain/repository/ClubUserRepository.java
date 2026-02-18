package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubUserRepository {
  ClubUser save(ClubUser clubUser);

  ClubUser getUserByClubAndEmail(Long clubId, String email);

  void delete(Long clubId, String email);

  Page<ClubUser> getAll(Long clubId, Pageable pageable);
}
