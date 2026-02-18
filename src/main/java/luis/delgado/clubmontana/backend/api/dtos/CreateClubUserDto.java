package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateClubUserDto(
    @NotBlank String nif,
    @NotBlank String name,
    String surname,
    @Email @NotBlank String email,
    @NotNull LocalDate birthDate,
    @NotBlank String address,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank String postalCode,
    @NotBlank String phone,
    String homePhone) {}
