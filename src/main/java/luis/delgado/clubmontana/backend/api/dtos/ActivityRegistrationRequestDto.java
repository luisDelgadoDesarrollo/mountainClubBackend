package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ActivityRegistrationRequestDto(
    @NotBlank String name,
    @NotBlank String surname,
    @NotBlank String nif,
    @NotBlank @Email String email,
    String federateNumber) {}
