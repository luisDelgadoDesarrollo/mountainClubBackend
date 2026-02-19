package luis.delgado.clubmontana.backend.api.dtos;

import java.time.LocalDate;

public record ClubUserDto(
    Long clubId,
    String nif,
    String name,
    String surname,
    String email,
    String federatedNumber,
    LocalDate birthDate,
    String address,
    String city,
    String state,
    String postalCode,
    String phone,
    String homePhone) {}
