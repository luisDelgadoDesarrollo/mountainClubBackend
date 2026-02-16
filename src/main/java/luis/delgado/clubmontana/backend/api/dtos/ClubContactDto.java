package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Pattern;

public record ClubContactDto(
    Long clubId,
    String contactEmail,
    @Pattern(
            regexp = "^(?:$|\\+?[1-9]\\d{7,14}(?:\\s?(?:ext\\.?|x|#)\\s?\\d{1,5})?)$",
            message =
                "El teléfono debe ser válido y la extensión (si existe) debe tener 1 a 5 dígitos")
        String phone) {}
