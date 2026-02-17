package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface ActivityUseCases {
  Activity createActivity(Long clubId, Activity activity, Map<String, MultipartFile> files);

  Activity updateActivity(
      Long clubId, Long activityId, Activity activity, Map<String, MultipartFile> files);

  void deleteActivity(Long clubId, Long activityId);

  Pair<Activity, List<String>> getActivity(Long clubId, Long activityId);

  List<Pair<Activity, List<String>>> getAllActivity(Long clubId, Pageable pageable);

  Optional<Pair<Activity, List<String>>> getLastActivity(Long clubId);

  List<Pair<Activity, List<String>>> getAllByYearActivity(Long clubId, Integer year);

  List<Integer> getYears(Long clubId);
}
