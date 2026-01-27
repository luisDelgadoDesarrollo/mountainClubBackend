package luis.delgado.clubmontana.backend.domain.model.commands.createUser;

import java.time.LocalDate;
import lombok.Builder;
import luis.delgado.clubmontana.backend.domain.model.enums.Sex;

@Builder
public record CreateUserCommand(
    String email,
    String password,
    String firstName,
    String lastName,
    String dni,
    LocalDate birthDate,
    Sex sex,
    String phone,
    String address,
    String city,
    String postalCode,
    String country) {}
