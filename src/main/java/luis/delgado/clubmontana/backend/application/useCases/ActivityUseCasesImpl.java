package luis.delgado.clubmontana.backend.application.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.api.exceptions.BadDateActivity;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.services.SlugFactory;
import luis.delgado.clubmontana.backend.domain.userCases.ActivityUseCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class ActivityUseCasesImpl implements ActivityUseCases {
  private final ActivityRepository activityRepository;
  private final FileStorageService fileStorageService;
  private final SlugFactory slugFactory;

  public ActivityUseCasesImpl(
      ActivityRepository activityRepository,
      FileStorageService fileStorageService,
      SlugFactory slugFactory) {
    this.activityRepository = activityRepository;
    this.fileStorageService = fileStorageService;
    this.slugFactory = slugFactory;
  }

  @Override
  public Activity createActivity(Long clubId, Activity activity, Map<String, MultipartFile> files) {
    activity.setClubId(clubId);
    activity.setSlug(slugFactory.makeSlug(activity.getTitle()));
    Activity activitySaved = chekActivity(activity);
    fileStorageService.store(
        files,
        activitySaved.getImages().stream()
            .collect(Collectors.toMap(Image::getImage, Image::getImageId)),
        activitySaved.getActivityId(),
        clubId,
        ImageType.ACTIVITY);
    return activitySaved;
  }

  @Override
  public Activity updateActivity(
      Long clubId, Long activityId, Activity activity, Map<String, MultipartFile> files) {
    activity.setClubId(clubId);
    activity.setActivityId(activityId);
    Activity activitySaved = chekActivity(activity);
    fileStorageService.deleteImages(clubId, ImageType.ACTIVITY, activityId);
    fileStorageService.store(
        files,
        activitySaved.getImages().stream()
            .collect(Collectors.toMap(Image::getImage, Image::getImageId)),
        activitySaved.getActivityId(),
        clubId,
        ImageType.ACTIVITY);
    return activitySaved;
  }

  @Override
  public void deleteActivity(Long clubId, Long activityId) {
    activityRepository.deleteActivity(clubId, activityId);
    fileStorageService.deleteImages(clubId, ImageType.ACTIVITY, activityId);
  }

  @NoAuthenticationNeeded
  @Override
  public Pair<Activity, List<String>> getActivity(Long clubId, Long activityId) {
    Activity activity = activityRepository.getActivity(clubId, activityId);
    List<String> imagesPath = fileStorageService.getImages(clubId, activityId, ImageType.ACTIVITY);
    return Pair.of(activity, imagesPath);
  }

  @NoAuthenticationNeeded
  @Override
  public List<Pair<Activity, List<String>>> getAllActivity(Long clubId, Pageable pageable) {
    List<Pair<Activity, List<String>>> activitiesWithPath = new ArrayList<>();
    Page<Activity> activities = activityRepository.getActivities(clubId, pageable);
    activities.forEach(
        activity ->
            activitiesWithPath.add(
                Pair.of(
                    activity,
                    fileStorageService.getImages(
                        clubId, activity.getActivityId(), ImageType.ACTIVITY))));
    return activitiesWithPath;
  }

  private Activity chekActivity(Activity activity) throws BadDateActivity {
    if (activity.getNoAffiliatePrice() == null)
      activity.setNoAffiliatePrice(activity.getAffiliatePrice());
    if (activity.getEndDate() == null) activity.setEndDate(activity.getStartDate());
    if (activity.getEndDate().isBefore(activity.getStartDate()))
      throw new BadDateActivity("La fecha de final es anterior a la fecha de fin");
    return activityRepository.saveActivity(activity);
  }
}
