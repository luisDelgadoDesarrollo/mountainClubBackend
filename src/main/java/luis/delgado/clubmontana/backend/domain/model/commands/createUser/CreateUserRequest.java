package luis.delgado.clubmontana.backend.domain.model.commands.createUser;

import java.time.LocalDate;
import lombok.*;
import luis.delgado.clubmontana.backend.domain.model.enums.Sex;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
  String email;
  String password;
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
}
