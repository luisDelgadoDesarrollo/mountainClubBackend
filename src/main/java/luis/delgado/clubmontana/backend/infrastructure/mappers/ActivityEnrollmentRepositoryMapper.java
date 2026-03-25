package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.ActivityEnrollment;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEnrollmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityEnrollmentRepositoryMapper {

    ActivityEnrollment activityEnrollmentEntityToActivityEnrollment(
            ActivityEnrollmentEntity activityEnrollmentEntity);

    ActivityEnrollmentEntity activityEntollmentToActivityEnrollmentEntity(
            ActivityEnrollment activityEnrollment);
}
