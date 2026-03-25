package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import luis.delgado.clubmontana.backend.core.annotations.ValidSpanishNif;

public record ActivityRegistrationRequestDto(
    @NotBlank String name,
    @NotBlank String surname,
    @NotBlank @ValidSpanishNif String nif,
    @NotBlank @Email String email,
    String federateNumber) {}
