package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.api.exceptions.ActivityNotFoundException;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.ActivityRepositoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class ActivityRepositoryImpl implements ActivityRepository {

  private final ActivityEntityJpa activityEntityJpa;
  private final ActivityRepositoryMapper activityRepositoryMapper;

  public ActivityRepositoryImpl(
      ActivityEntityJpa activityEntityJpa, ActivityRepositoryMapper activityRepositoryMapper) {
    this.activityEntityJpa = activityEntityJpa;
    this.activityRepositoryMapper = activityRepositoryMapper;
  }

  @Override
  public Activity saveActivity(Activity activity) {
    return activityRepositoryMapper.activityEntityToActivity(
        activityEntityJpa.save(activityRepositoryMapper.activityToActivityEntity(activity)));
  }

  @Override
  public void deleteActivity(Long clubId, Long activityId) {
    activityEntityJpa.deleteByClub_ClubIdAndActivityId(clubId, activityId);
  }

  @Override
  public Activity getActivity(Long clubId, Long activityId) {
    return activityEntityJpa
        .findByClubAndId(clubId, activityId)
        .map(activityRepositoryMapper::activityEntityToActivity)
        .orElseThrow(() -> new ActivityNotFoundException(clubId, activityId));
  }

  @Override
  public Page<Activity> getActivities(Long clubId, Pageable pageable) {
    return activityEntityJpa
        .findByClub_ClubId(clubId, pageable)
        .map(activityRepositoryMapper::activityEntityToActivity);
  }

  @Override
  public Boolean existBySlug(String s) {
    return activityEntityJpa.existsBySlug(s);
  }
}
