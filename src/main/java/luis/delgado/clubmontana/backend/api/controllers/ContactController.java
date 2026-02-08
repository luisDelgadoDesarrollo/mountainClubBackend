package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import luis.delgado.clubmontana.backend.api.dtos.ContactRequestDto;
import luis.delgado.clubmontana.backend.api.mappers.ContactControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.ContactUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/clubs/{clubId}")
@RestController
public class ContactController {

    private final ContactControllerMapper contactControllerMapper;
    private final ContactUseCases contactUseCases;

    public ContactController(ContactControllerMapper contactControllerMapper, ContactUseCases contactUseCases) {
        this.contactControllerMapper = contactControllerMapper;
        this.contactUseCases = contactUseCases;
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> contact(@PathVariable Long clubId, @RequestBody @Valid ContactRequestDto contactRequestDto){
        contactUseCases.contact(clubId, contactControllerMapper.contactRequestDtoToContactRequest(contactRequestDto));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
