package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import luis.delgado.clubmontana.backend.api.dtos.ContactRequestDto;
import luis.delgado.clubmontana.backend.api.mappers.ContactControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.userCases.ContactUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/clubs/{club}/contact")
@RestController
public class ContactController {

  private final ContactControllerMapper contactControllerMapper;
  private final ContactUseCases contactUseCases;

  public ContactController(
      ContactControllerMapper contactControllerMapper, ContactUseCases contactUseCases) {
    this.contactControllerMapper = contactControllerMapper;
    this.contactUseCases = contactUseCases;
  }

  @PostMapping("/message")
  public ResponseEntity<Void> contact(
      @ClubId Long clubId, @RequestBody @Valid ContactRequestDto contactRequestDto) {
    contactUseCases.contact(
        clubId, contactControllerMapper.contactRequestDtoToContactRequest(contactRequestDto));
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/memberShipRequest")
  public ResponseEntity<Void> memberShipSingup(
      @ClubId Long clubId,
      @RequestParam("signUp") MultipartFile signUp,
      @RequestParam("receipt") MultipartFile receipt) {
    contactUseCases.memberShipSingup(clubId, signUp, receipt);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/federation")
  public ResponseEntity<Void> federation(
      @ClubId Long clubId, @RequestParam("signUp") MultipartFile dataResponsibility) {
    contactUseCases.federation(clubId, dataResponsibility);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
