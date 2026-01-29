package luis.delgado.clubmontana.backend.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenResponseDto(
    @NotBlank @Schema(example = "jwt-token") String accessToken,
    @NotBlank @Schema(example = "Bearer") String tokenType,
    @NotNull @Schema(example = "3600") Long expiresIn,
    @NotBlank @Schema(example = "tokenRandom") String refreshToken) {}
