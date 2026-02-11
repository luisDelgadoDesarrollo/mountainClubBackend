package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityRepository {
  Activity saveActivity(Activity activity);

  void deleteActivity(Long clubId, Long activityId);

  Activity getActivity(Long clubId, Long activityId);

  Page<Activity> getActivities(Long clubId, Pageable pageable);

  Boolean existBySlug(String s);
}
