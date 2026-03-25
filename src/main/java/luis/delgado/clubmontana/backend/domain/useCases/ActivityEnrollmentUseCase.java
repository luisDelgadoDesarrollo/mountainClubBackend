package luis.delgado.clubmontana.backend.domain.useCases;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.ActivityEnrollment;

public interface ActivityEnrollmentUseCase {
  void save(Long clubId, ActivityEnrollment activityEnrollment);

  List<ActivityEnrollment> getActivityEnrollments(Long clubId, Long activityId);
}
