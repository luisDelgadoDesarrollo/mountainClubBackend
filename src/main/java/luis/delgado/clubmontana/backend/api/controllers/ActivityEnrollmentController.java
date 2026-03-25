package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.ActivityEnrollmentDto;
import luis.delgado.clubmontana.backend.api.mappers.ActivityEnrollmentControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ActivityId;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.useCases.ActivityEnrollmentUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/{club}/activityEnrollment")
public class ActivityEnrollmentController {

  private final ActivityEnrollmentUseCase activityEnrollmentUseCase;
  private final ActivityEnrollmentControllerMapper activityEnrollmentControllerMapper;

  public ActivityEnrollmentController(
      ActivityEnrollmentUseCase activityEnrollmentUseCase,
      ActivityEnrollmentControllerMapper activityEnrollmentControllerMapper) {
    this.activityEnrollmentUseCase = activityEnrollmentUseCase;
    this.activityEnrollmentControllerMapper = activityEnrollmentControllerMapper;
  }

  @PostMapping
  public ResponseEntity<Void> saveActivityEnrollment(
      @ClubId Long clubId, @RequestBody @Valid ActivityEnrollmentDto activityEnrollmentDto) {
    activityEnrollmentUseCase.save(
        clubId,
        activityEnrollmentControllerMapper.activityEnrollmentDtoToActivityEnrollment(
            activityEnrollmentDto));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{activity}")
  public ResponseEntity<List<ActivityEnrollmentDto>> getActivityEnrollments(
      @ClubId Long clubId, @ActivityId Long activityId) {
    return ResponseEntity.ok(
        activityEnrollmentUseCase.getActivityEnrollments(clubId, activityId).stream()
            .map(activityEnrollmentControllerMapper::activityEnrollmentToActivityEnrollmentDto)
            .toList());
  }
}
