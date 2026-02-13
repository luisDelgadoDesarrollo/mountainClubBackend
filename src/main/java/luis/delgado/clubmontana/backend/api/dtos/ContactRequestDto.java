package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContactRequestDto(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String message,
    @Pattern(regexp = "^$|^[+]?[0-9]{7,15}$", message = "Número de teléfono inválido")
        String phoneNumber) {}
