package luis.delgado.clubmontana.backend.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenResponseDto(
    @NotBlank @Schema(example = "jwt-token") String accessToken,
    @NotNull @Schema(example = "3600") Long expiresIn) {}
