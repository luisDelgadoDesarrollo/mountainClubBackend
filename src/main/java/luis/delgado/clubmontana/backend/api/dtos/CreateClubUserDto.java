package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import luis.delgado.clubmontana.backend.core.annotations.ValidSpanishNif;

public record CreateClubUserDto(
    @NotBlank @ValidSpanishNif String nif,
    @NotBlank String name,
    @NotBlank String surname,
    @Email @NotBlank String email,
    String federatedNumber,
    @NotNull LocalDate birthDate,
    @NotBlank String address,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank String postalCode,
    @NotBlank String phone,
    String homePhone) {}
