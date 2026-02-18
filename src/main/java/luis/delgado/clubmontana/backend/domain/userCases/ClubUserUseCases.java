package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubUserUseCases {

  ClubUser create(Long clubId, ClubUser clubUser);

  ClubUser get(Long clubId, String email);

  ClubUser update(Long clubId, String email, ClubUser clubUserDtoToCreateClubUser);

  void delete(Long clubId, String email);

  Page<ClubUser> getAll(Long clubId, Pageable pageable);
}
