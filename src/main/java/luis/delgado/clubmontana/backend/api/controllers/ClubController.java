package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import luis.delgado.clubmontana.backend.api.dtos.ClubContactDto;
import luis.delgado.clubmontana.backend.api.dtos.ClubRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.ClubResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.ClubControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
public class ClubController {

  private final ClubUseCases clubUseCases;
  private final ClubControllerMapper clubControllerMapper;

  public ClubController(ClubUseCases clubUseCases, ClubControllerMapper clubControllerMapper) {
    this.clubUseCases = clubUseCases;
    this.clubControllerMapper = clubControllerMapper;
  }

  @PostMapping
  public ResponseEntity<ClubResponseDto> createClub(
      @Valid @RequestBody ClubRequestDto clubRequestDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            clubControllerMapper.createCLubResponseToClubResponseDto(
                clubUseCases.createClub(
                    clubControllerMapper.clubRequestDtoToCreateCLubCommand(clubRequestDto))));
  }

  @GetMapping("/{club}/contactInfo")
  public ResponseEntity<ClubContactDto> getClubContact(@ClubId Long clubId) {
    return ResponseEntity.ok(
        clubControllerMapper.clubToClubContactDto(clubUseCases.getClub(clubId)));
  }

  @PutMapping("/{club}/contactInfo")
  public ResponseEntity<ClubContactDto> updateClubContact(
      @ClubId Long clubId, @Valid @RequestBody ClubContactDto clubContactDto) {
    clubUseCases.updateContact(clubId, clubContactDto.phone(), clubContactDto.contactEmail());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
