package luis.delgado.clubmontana.backend.domain.repository;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.ActivityEnrollment;

public interface ActivityEnrollmentRepository {
  void save(ActivityEnrollment activityEnrollment);

  Integer getEnrollments(Long activityId);

  List<ActivityEnrollment> getActivityEnrollments(Long activityId);
}
