package luis.delgado.clubmontana.backend.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank
        @Schema(example = "socio@clubmontana.es")
        String username,

        @NotBlank
        @Schema(example = "passwordSeguro123")
        String password

) {}
