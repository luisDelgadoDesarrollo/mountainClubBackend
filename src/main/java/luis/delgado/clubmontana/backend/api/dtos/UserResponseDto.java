package luis.delgado.clubmontana.backend.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UserResponseDto(
    @NotBlank @Schema(example = "1") Long userId,
    @NotBlank @Email @Schema(example = "socio@clubmontana.es") String email,
    @NotBlank @Schema(example = "Juan") String firstName,
    @NotBlank @Schema(example = "Pérez") String lastName,
    @Schema(example = "12345678Z") String dni,
    @Past @Schema(example = "1990-05-12") LocalDate birthDate,
    @Pattern(regexp = "[MFON]") @Schema(example = "M") String sex,
    @Schema(example = "+34 600 123 456") String phone,
    @Schema(example = "Calle Mayor 10") String address,
    @Schema(example = "Zaragoza") String city,
    @Schema(example = "50001") String postalCode,
    @Schema(example = "España") String country,
    @Schema(example = "Activado") String response) {}
