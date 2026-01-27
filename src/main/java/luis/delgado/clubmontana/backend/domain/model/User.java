package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserCommand;
import luis.delgado.clubmontana.backend.domain.model.enums.Sex;

@Data
@Builder
public class User {
  Long userId;
  String email;
  String firstName;
  String lastName;
  String dni;
  LocalDate birthDate;
  Sex sex;
  String phone;
  String address;
  String city;
  String postalCode;
  String country;
  Boolean active;
  Boolean emailVerified;

  public static User fromCommand(CreateUserCommand command) {
    return User.builder()
        .email(command.email())
        .firstName(command.firstName())
        .lastName(command.lastName())
        .dni(command.dni())
        .birthDate(command.birthDate())
        .sex(command.sex())
        .phone(command.phone())
        .address(command.address())
        .city(command.city())
        .postalCode(command.postalCode())
        .country(command.country())
        .build();
  }
}
