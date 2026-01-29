package luis.delgado.clubmontana.backend.api.controllers;

import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.PublicationControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping
  public ResponseEntity<PublicationResponseDto> createPublication(
      @PathVariable Long clubId, @RequestBody CreatePublicationRequestDto request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            publicationControllerMapper.createPublicationResponseToPublicationResponseDto(
                publicationUseCases.create(
                    clubId,
                    publicationControllerMapper.publicationRequestDtoToCreatePublicationCommand(
                        request))));
  }
}
