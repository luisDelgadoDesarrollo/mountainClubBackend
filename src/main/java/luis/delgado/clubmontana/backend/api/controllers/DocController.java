package luis.delgado.clubmontana.backend.api.controllers;

import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.model.enums.PdfType;
import luis.delgado.clubmontana.backend.domain.userCases.DocUseCase;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs/{club}/doc")
public class DocController {

  private final DocUseCase docUseCase;

  public DocController(DocUseCase docUseCase) {
    this.docUseCase = docUseCase;
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> saveBylaws(
      @ClubId Long clubId,
      @RequestParam("file") MultipartFile file,
      @RequestParam PdfType pdfType) {
    docUseCase.save(clubId, file, pdfType);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<Resource> getUs(@ClubId Long clubId, @RequestParam PdfType pdfType) {
    return ResponseEntity.ok(docUseCase.get(clubId, pdfType));
  }
}
