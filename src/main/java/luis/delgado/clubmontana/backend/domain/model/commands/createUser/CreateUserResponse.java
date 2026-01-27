package luis.delgado.clubmontana.backend.domain.model.commands.createUser;

import java.time.LocalDate;
import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.enums.Sex;

public record CreateUserResponse(
    Long userId,
    String email,
    String firstName,
    String lastName,
    String dni,
    LocalDate birthDate,
    Sex sex,
    String phone,
    String address,
    String city,
    String postalCode,
    String country,
    String response) {

  public static CreateUserResponse fromUser(User user, String response) {
    return new CreateUserResponse(
        user.getUserId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getDni(),
        user.getBirthDate(),
        user.getSex(),
        user.getPhone(),
        user.getAddress(),
        user.getCity(),
        user.getPostalCode(),
        user.getCountry(),
        response);
  }
}
