package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityImageEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityRepositoryMapper {

  default ActivityEntity activityToActivityEntity(Activity activity) {
    if (activity == null) return null;
    ActivityEntity activityEntity = new ActivityEntity();
    activityEntity.setActivityId(activity.getActivityId());
    activityEntity.setTitle(activity.getTitle());
    activityEntity.setDescription(activity.getDescription());
    ClubEntity club = new ClubEntity();
    club.setClubId(activity.getClubId());
    activityEntity.setClub(club);
    activityEntity.setAffiliatePrice(activity.getAffiliatePrice());
    activityEntity.setNoAffiliatePrice(activity.getNoAffiliatePrice());
    activityEntity.setStartDate(activity.getStartDate());
    activityEntity.setEndDate(activity.getEndDate());
    if (activity.getImages() != null) {
      activity
          .getImages()
          .forEach(
              activityImage ->
                  activityEntity.addImage(activityImageToActivityImageEntity(activityImage)));
    }
    return activityEntity;
  }

  @Mapping(target = "clubId", source = "club.clubId")
  Activity activityEntityToActivity(ActivityEntity activityEntity);

  @Mapping(target = "imageId", source = "activity.activityId")
  Image activityImageEntityToActivityImage(ActivityImageEntity activityImageEntity);

  @Mapping(target = "activity.activityId", source = "imageId")
  ActivityImageEntity activityImageToActivityImageEntity(Image image);
}
