package luis.delgado.clubmontana.backend.api.controllers;

import java.util.Map;
import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.PublicationControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/publications/{clubId}")
public class PublicationController {

  private final PublicationControllerMapper publicationControllerMapper;
  private final PublicationUseCases publicationUseCases;

  public PublicationController(
      PublicationControllerMapper publicationControllerMapper,
      PublicationUseCases publicationUseCases) {
    this.publicationControllerMapper = publicationControllerMapper;
    this.publicationUseCases = publicationUseCases;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<PublicationResponseDto> createPublication(
      @PathVariable Long clubId,
      @RequestPart("data") CreatePublicationRequestDto request,
      @RequestParam Map<String, MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            publicationControllerMapper.publicationToPublicationResponseDto(
                publicationUseCases.create(
                    clubId,
                    publicationControllerMapper.publicationRequestDtoToCreatePublicationCommand(
                        request),
                    files)));
  }
}
