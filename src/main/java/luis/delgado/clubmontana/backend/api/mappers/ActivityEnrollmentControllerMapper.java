package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.ActivityEnrollmentDto;
import luis.delgado.clubmontana.backend.domain.model.ActivityEnrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityEnrollmentControllerMapper {
  ActivityEnrollmentDto activityEnrollmentToActivityEnrollmentDto(
      ActivityEnrollment activityEnrollment);

  ActivityEnrollment activityEnrollmentDtoToActivityEnrollment(
      ActivityEnrollmentDto activityEnrollmentDto);
}
