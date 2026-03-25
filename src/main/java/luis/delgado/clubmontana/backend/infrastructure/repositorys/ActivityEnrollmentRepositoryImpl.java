package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.ActivityEnrollment;
import luis.delgado.clubmontana.backend.domain.repository.ActivityEnrollmentRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEnrollmentEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.ActivityEnrollmentRepositoryMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityEnrollmentRepositoryImpl implements ActivityEnrollmentRepository {

  private final ActivityEnrollmentEntityJpa activityEnrollmentEntityJpa;
  private final ActivityEnrollmentRepositoryMapper activityEnrollmentRepositoryMapper;

  public ActivityEnrollmentRepositoryImpl(
      ActivityEnrollmentEntityJpa activityEnrollmentEntityJpa,
      ActivityEnrollmentRepositoryMapper activityEnrollmentRepositoryMapper) {
    this.activityEnrollmentEntityJpa = activityEnrollmentEntityJpa;
    this.activityEnrollmentRepositoryMapper = activityEnrollmentRepositoryMapper;
  }

  @Override
  public void save(ActivityEnrollment activityEnrollment) {
    activityEnrollmentEntityJpa.save(
        activityEnrollmentRepositoryMapper.activityEntollmentToActivityEnrollmentEntity(
            activityEnrollment));
  }

  @Override
  public Integer getEnrollments(Long activityId) {
    return activityEnrollmentEntityJpa.countByActivityId(activityId);
  }

  @Override
  public List<ActivityEnrollment> getActivityEnrollments(Long activityId) {
    return activityEnrollmentEntityJpa.findByActivityId(activityId).stream()
        .map(activityEnrollmentRepositoryMapper::activityEnrollmentEntityToActivityEnrollment)
        .toList();
  }
}
