package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.constraints.Null;
import java.util.Map;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.UsRequestDto;
import luis.delgado.clubmontana.backend.api.mappers.UsControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.UsUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/us/{clubId}")
public class UsController {

  private final UsUseCases usUseCases;
  private final UsControllerMapper usControllerMapper;

  public UsController(UsUseCases usUseCases, UsControllerMapper usControllerMapper) {
    this.usUseCases = usUseCases;
    this.usControllerMapper = usControllerMapper;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Null> createUs(
      @PathVariable Long clubId,
      @RequestParam UsRequestDto us,
      @RequestParam Map<String, MultipartFile> files) {
    usUseCases.create(clubId, usControllerMapper.usRequestDroToUsRequest(us), files);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Null> updateUs(
      @PathVariable Long clubId,
      @RequestParam UsRequestDto us,
      @RequestParam Map<String, MultipartFile> files) {
    usUseCases.update(clubId, usControllerMapper.usRequestDroToUsRequest(us), files);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<PublicationResponseDto> getUs(@PathVariable Long clubId) {
    return ResponseEntity.ok(usControllerMapper.useResponseToUsResponseDto(usUseCases.get(clubId)));
  }
}
