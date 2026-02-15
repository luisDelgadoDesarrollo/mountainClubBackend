package luis.delgado.clubmontana.backend.api.controllers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.CreatePublicationRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.PublicationResponseDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.PublicationControllerMapper;
import luis.delgado.clubmontana.backend.application.services.RequestPartUtilsImpl;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.core.annotations.PublicationId;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.springframework.data.domain.Page;
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
  private final RequestPartUtilsImpl requestPartUtilsImpl;

  public PublicationController(
      PublicationControllerMapper publicationControllerMapper,
      PublicationUseCases publicationUseCases,
      RequestPartUtilsImpl requestPartUtilsImpl) {
    this.publicationControllerMapper = publicationControllerMapper;
    this.publicationUseCases = publicationUseCases;
    this.requestPartUtilsImpl = requestPartUtilsImpl;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseDto> createPublication(
      @ClubId Long clubId,
      @RequestPart("data") String data,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    CreatePublicationRequestDto request =
        requestPartUtilsImpl.parseAndValidate(data, CreatePublicationRequestDto.class);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            publicationControllerMapper.publicationToPublicationResponseDto(
                publicationUseCases.create(
                    clubId,
                    publicationControllerMapper.publicationRequestDtoToCreatePublicationCommand(
                        request),
                    requestPartUtilsImpl.toFileMap(files))));
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
      @RequestPart("data") String data,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    CreatePublicationRequestDto request =
        requestPartUtilsImpl.parseAndValidate(data, CreatePublicationRequestDto.class);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            publicationControllerMapper.publicationToPublicationResponseDto(
                publicationUseCases.update(
                    clubId,
                    publicationId,
                    publicationControllerMapper.publicationRequestDtoToCreatePublicationCommand(
                        request),
                    requestPartUtilsImpl.toFileMap(files))));
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

  @GetMapping("/last")
  public ResponseEntity<PublicationResponseDto> getLastPublication(@ClubId Long clubId) {
    return publicationUseCases
        .getLastPublication(clubId)
        .map(
            pair ->
                ResponseEntity.ok(
                    publicationControllerMapper.publicationResponseToPublicationResponseDto(
                        pair.getFirst(), pair.getSecond())))
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @GetMapping
  public ResponseEntity<Page<PublicationResponseDto>> getPublications(
      @ClubId Long clubId, Pageable pageable) {
    return ResponseEntity.ok(
        publicationUseCases
            .getPublications(clubId, pageable)
            .map(
                pair ->
                    publicationControllerMapper.publicationResponseToPublicationResponseDto(
                        pair.getFirst(), pair.getSecond())));
  }
}
