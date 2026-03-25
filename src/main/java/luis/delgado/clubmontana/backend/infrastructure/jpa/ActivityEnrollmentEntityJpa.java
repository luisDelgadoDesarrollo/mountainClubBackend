package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.List;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEnrollmentEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ActivityEnrollmentIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityEnrollmentEntityJpa
    extends JpaRepository<ActivityEnrollmentEntity, ActivityEnrollmentIdEntity> {
  Integer countByActivityId(Long activityId);

  List<ActivityEnrollmentEntity> findByActivityId(Long activityId);
}
