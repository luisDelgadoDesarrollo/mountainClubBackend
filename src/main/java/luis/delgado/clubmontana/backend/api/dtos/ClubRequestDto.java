package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.*;

public record ClubRequestDto(
    @NotBlank @Size(max = 255) String name,
    @NotBlank @Size(max = 50) String nif,
    @NotBlank @Email @Size(max = 255) String email,
    @NotBlank
        @Size(max = 255)
        @Pattern(regexp = "^(https?://).+", message = "La URL debe empezar por http:// o https://")
        String url,
    String description,
    @Size(max = 255) String logo,
    @NotNull Boolean hasInicio,
    @NotNull Boolean hasSecciones,
    @NotNull Boolean hasGaleria,
    @NotNull Boolean hasEnlaces,
    @NotNull Boolean hasContacto,
    @NotNull Boolean hasFederarse,
    @NotNull Boolean hasTienda,
    @NotNull Boolean hasCalendario,
    @NotNull Boolean hasConocenos,
    @NotNull Boolean hasNoticias,
    @NotNull Boolean hasForo,
    @NotNull Boolean hasEstatutos,
    @NotNull Boolean hasNormas,
    @NotNull Boolean hasHazteSocio) {}
