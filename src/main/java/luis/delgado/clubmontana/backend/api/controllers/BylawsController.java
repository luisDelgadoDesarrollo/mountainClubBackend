package luis.delgado.clubmontana.backend.api.controllers;

import luis.delgado.clubmontana.backend.domain.userCases.BylawsUseCase;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bylaws/{clubId}")
public class BylawsController {

  private final BylawsUseCase bylawsUseCase;

  public BylawsController(BylawsUseCase bylawsUseCase) {
    this.bylawsUseCase = bylawsUseCase;
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> saveBylaws(
      @PathVariable Long clubId, @RequestParam("file") MultipartFile file) {
    bylawsUseCase.save(clubId, file);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<Resource> getUs(@PathVariable Long clubId) {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=estatutos.pdf")
        .body(bylawsUseCase.getBylaws(clubId));
  }
}
