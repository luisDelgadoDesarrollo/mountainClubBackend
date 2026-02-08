package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.api.exceptions.UsNotFoundException;
import luis.delgado.clubmontana.backend.domain.model.Us;
import luis.delgado.clubmontana.backend.domain.repository.UsRepository;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsImageEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UsEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.UsRepositoryMapper;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class UsRepositoryImpl implements UsRepository {
  private final UsEntityJpa usEntityJpa;
  private final UsRepositoryMapper usRepositoryMapper;
  private final ClubEntityJpa clubEntityJpa;

  public UsRepositoryImpl(
      UsEntityJpa usEntityJpa, UsRepositoryMapper usRepositoryMapper, ClubEntityJpa clubEntityJpa) {
    this.usEntityJpa = usEntityJpa;
    this.usRepositoryMapper = usRepositoryMapper;
    this.clubEntityJpa = clubEntityJpa;
  }

  @Override
  public Us save(Us us, Long clubId) {
    ClubEntity club = clubEntityJpa.findById(clubId).orElseThrow();
    us.getImages().forEach(image -> image.setClubId(clubId));
    UsEntity usEntity = usRepositoryMapper.usToUsEntity(us);
    usEntity.setClub(club);
    return usRepositoryMapper.usEntityToUs(usEntityJpa.save(usEntity));
  }

  @Override
  @Transactional
  public Us update(Us us, Long clubId) {

    UsEntity usEntity =
        usEntityJpa.findById(clubId).orElseThrow(() -> new IllegalStateException("US no existe"));

    // campos simples
    usEntity.setText(us.getText());

    // imágenes
    usEntity.getImages().clear();
    us.getImages()
        .forEach(
            img -> {
              UsImageEntity imgEntity = usRepositoryMapper.usImageToUsImageEntity(img);
              imgEntity.setUs(usEntity);
              usEntity.getImages().add(imgEntity);
            });

    UsEntity saved = usEntityJpa.save(usEntity);
    return usRepositoryMapper.usEntityToUs(saved);
  }

  @Override
  public Us get(Long clubId) {
    return usRepositoryMapper.usEntityToUs(
        usEntityJpa.findByClub_ClubId(clubId).orElseThrow(() -> new UsNotFoundException(clubId)));
  }
}
