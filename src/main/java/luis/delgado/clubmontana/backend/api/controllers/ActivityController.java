package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.api.dtos.ActivityDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.SaveActivityDto;
import luis.delgado.clubmontana.backend.api.mappers.ActivityControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ActivityId;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.userCases.ActivityUseCases;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs/{club}/activities")
public class ActivityController {

  private final ActivityControllerMapper activityControllerMapper;
  private final ActivityUseCases activityUseCases;

  public ActivityController(
      ActivityControllerMapper activityControllerMapper, ActivityUseCases activityUseCases) {
    this.activityControllerMapper = activityControllerMapper;
    this.activityUseCases = activityUseCases;
  }

  @PostMapping
  public ResponseEntity<ResponseDto> create(
      @ClubId Long clubId,
      @RequestPart("activity") @Valid SaveActivityDto saveActivityDto,
      @RequestParam Map<String, MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            activityControllerMapper.activityToIdResponseDto(
                activityUseCases.createActivity(
                    clubId,
                    activityControllerMapper.saveActivityDtoToActivity(saveActivityDto),
                    files)));
  }

  @PutMapping("/{activity}")
  public ResponseEntity<ResponseDto> update(
      @ClubId Long clubId,
      @ActivityId Long activityId,
      @RequestPart("activity") @Valid SaveActivityDto saveActivityDto,
      @RequestParam Map<String, MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            activityControllerMapper.activityToIdResponseDto(
                activityUseCases.updateActivity(
                    clubId,
                    activityId,
                    activityControllerMapper.saveActivityDtoToActivity(saveActivityDto),
                    files)));
  }

  @DeleteMapping("/{activity}")
  public ResponseEntity<Void> delete(@ClubId Long clubId, @ActivityId Long activityId) {
    activityUseCases.deleteActivity(clubId, activityId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/{activity}")
  public ResponseEntity<ActivityDto> get(@ClubId Long clubId, @ActivityId Long activityId) {
    Pair<Activity, List<String>> activity = activityUseCases.getActivity(clubId, activityId);
    return ResponseEntity.ok(
        activityControllerMapper.activityWithPathToActivityDto(
            activity.getFirst(), activity.getSecond()));
  }

  @GetMapping()
  public ResponseEntity<List<ActivityDto>> getAll(@ClubId Long clubId, Pageable pageable) {
    return ResponseEntity.ok(
        activityControllerMapper.activityWithPathToActivityDto(
            activityUseCases.getAllActivity(clubId, pageable)));
  }
}
