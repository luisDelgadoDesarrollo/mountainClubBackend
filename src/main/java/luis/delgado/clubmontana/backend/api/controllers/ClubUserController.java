package luis.delgado.clubmontana.backend.api.controllers;

import luis.delgado.clubmontana.backend.api.dtos.ClubUserDto;
import luis.delgado.clubmontana.backend.api.dtos.CreateClubUserDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.ClubUserControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.userCases.ClubUserUseCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/{club}/users")
public class ClubUserController {

  private final ClubUserUseCases clubUserUseCases;
  private final ClubUserControllerMapper clubUserControllerMapper;

  public ClubUserController(
      ClubUserUseCases clubUserUseCases, ClubUserControllerMapper clubUserControllerMapper) {
    this.clubUserUseCases = clubUserUseCases;
    this.clubUserControllerMapper = clubUserControllerMapper;
  }

  @PostMapping
  public ResponseEntity<ResponseDto> create(
      @ClubId Long clubId, @RequestBody CreateClubUserDto createClubUserDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            clubUserControllerMapper.createClubToResponseDto(
                clubUserUseCases.create(
                    clubId,
                    clubUserControllerMapper.createClubUserDtoToCreateClubUser(
                        createClubUserDto))));
  }

  @PutMapping("/{email}")
  public ResponseEntity<ResponseDto> update(
      @ClubId Long clubId,
      @PathVariable String email,
      @RequestBody CreateClubUserDto createClubUserDto) {
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(
            clubUserControllerMapper.createClubToResponseDto(
                clubUserUseCases.update(
                    clubId,
                    email,
                    clubUserControllerMapper.createClubUserDtoToCreateClubUser(
                        createClubUserDto))));
  }

  @DeleteMapping("/{email}")
  public ResponseEntity<Void> delete(@ClubId Long clubId, @PathVariable String email) {
    clubUserUseCases.delete(clubId, email);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping()
  public ResponseEntity<Page<ClubUserDto>> getAll(
      @ClubId Long clubId,
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(
        clubUserUseCases
            .getAll(clubId, pageable)
            .map(clubUserControllerMapper::clubUserToClubUserDto));
  }
}
