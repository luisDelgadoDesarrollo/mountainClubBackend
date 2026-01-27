package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import luis.delgado.clubmontana.backend.api.dtos.ClubRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.ClubResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.ClubControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
