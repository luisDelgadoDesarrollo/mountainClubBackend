package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.domain.repository.ClubRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.ClubRepositoryMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ClubRepositoryImpl implements ClubRepository {
    private final ClubEntityJpa clubEntityJpa;
    private final ClubRepositoryMapper clubRepositoryMapper;

    public ClubRepositoryImpl(ClubEntityJpa clubEntityJpa, ClubRepositoryMapper clubRepositoryMapper) {
        this.clubEntityJpa = clubEntityJpa;
        this.clubRepositoryMapper = clubRepositoryMapper;
    }

    @Override
    public Club getById(Long clubId) {
        return clubEntityJpa.findById(clubId).map(clubRepositoryMapper::clubEntityToClub).orElseThrow();
    }
}
