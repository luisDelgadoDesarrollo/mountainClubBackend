package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import luis.delgado.clubmontana.backend.core.annotations.ValidSpanishNif;

public record ActivityEnrollmentDto(
    @NotNull Long activityId,
    @NotBlank String name,
    @NotBlank String surname,
    @NotBlank @ValidSpanishNif String nif,
    @Email @NotBlank String email,
    Boolean paid) {}
