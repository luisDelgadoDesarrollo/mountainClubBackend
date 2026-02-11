package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.PublicationControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.core.annotations.PublicationId;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs/{club}/publications")
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
  public ResponseEntity<ResponseDto> createPublication(
      @ClubId Long clubId,
      @RequestPart("data") @Valid CreatePublicationRequestDto request,
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

  @DeleteMapping("/{publication}")
  public ResponseEntity<Void> deletePublication(
      @ClubId Long clubId, @PublicationId Long publicationId) {
    publicationUseCases.delete(clubId, publicationId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping("/{publication}")
  public ResponseEntity<ResponseDto> updatePublication(
      @ClubId Long clubId,
      @PublicationId Long publicationId,
      @RequestPart("data") @Valid CreatePublicationRequestDto request,
      @RequestParam Map<String, MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            publicationControllerMapper.publicationToPublicationResponseDto(
                publicationUseCases.update(
                    clubId,
                    publicationId,
                    publicationControllerMapper.publicationRequestDtoToCreatePublicationCommand(
                        request),
                    files)));
  }

  @GetMapping("/{publication}")
  public ResponseEntity<PublicationResponseDto> getPublication(
      @ClubId Long clubId, @PublicationId Long publicationId) {
    Pair<Publication, List<String>> publicationResponse =
        publicationUseCases.getPublication(clubId, publicationId);
    return ResponseEntity.ok(
        publicationControllerMapper.publicationResponseToPublicationResponseDto(
            publicationResponse.getFirst(), publicationResponse.getSecond()));
  }

  @GetMapping
  public ResponseEntity<List<PublicationResponseDto>> getPublications(
      @ClubId Long clubId, Pageable pageable) {
    return ResponseEntity.ok(
        publicationControllerMapper.publicationsWithPathToPublicationResponseDtoList(
            publicationUseCases.getPublications(clubId, pageable)));
  }
}
