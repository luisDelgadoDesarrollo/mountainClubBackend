package luis.delgado.clubmontana.backend.api.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.ActivityDto;
import luis.delgado.clubmontana.backend.api.dtos.IdResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.SaveActivityDto;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.util.Pair;

@Mapper(componentModel = "spring")
public interface ActivityControllerMapper {

  SaveActivityDto activityToCreateActivityDto(Activity activity);

  Activity saveActivityDtoToActivity(SaveActivityDto saveActivityDto);

  @Mapping(target = "id", source = "activity.activityId")
  IdResponseDto activityToIdResponseDto(Activity activity);

  ActivityDto activityWithPathToActivityDto(Activity activity, List<String> imagesPath);

  default List<ActivityDto> activityWithPathToActivityDto(
      List<Pair<Activity, List<String>>> allActivity) {
    return allActivity.stream()
        .map(pair -> activityWithPathToActivityDto(pair.getFirst(), pair.getSecond()))
        .toList();
  }
}
