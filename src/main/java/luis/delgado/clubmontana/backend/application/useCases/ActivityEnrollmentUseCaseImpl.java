package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.ActivityEnrollment;
import luis.delgado.clubmontana.backend.domain.repository.ActivityEnrollmentRepository;
import luis.delgado.clubmontana.backend.domain.useCases.ActivityEnrollmentUseCase;

@UseCase
public class ActivityEnrollmentUseCaseImpl implements ActivityEnrollmentUseCase {
  private final ActivityEnrollmentRepository activityEnrollmentRepository;

  public ActivityEnrollmentUseCaseImpl(ActivityEnrollmentRepository activityEnrollmentRepository) {
    this.activityEnrollmentRepository = activityEnrollmentRepository;
  }

  @Override
  public void save(Long clubId, ActivityEnrollment activityEnrollment) {
    activityEnrollmentRepository.save(activityEnrollment);
  }

  @Override
  public List<ActivityEnrollment> getActivityEnrollments(Long clubId, Long activityId) {
    return activityEnrollmentRepository.getActivityEnrollments(activityId);
  }
}
