package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserRequest;
import luis.delgado.clubmontana.backend.domain.model.enums.Sex;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Long userId;
  private String email;
  private String firstName;
  private String lastName;
  private String dni;
  private LocalDate birthDate;
  private Sex sex;
  private String phone;
  private String address;
  private String city;
  private String postalCode;
  private String country;
  private Boolean emailVerified;
  private Long clubId;

  public static User fromCommand(CreateUserRequest command) {
    return User.builder()
        .email(command.getEmail())
        .firstName(command.getFirstName())
        .lastName(command.getLastName())
        .dni(command.getDni())
        .birthDate(command.getBirthDate())
        .sex(command.getSex())
        .phone(command.getPhone())
        .address(command.getAddress())
        .city(command.getCity())
        .postalCode(command.getPostalCode())
        .country(command.getCountry())
        .build();
  }
}
