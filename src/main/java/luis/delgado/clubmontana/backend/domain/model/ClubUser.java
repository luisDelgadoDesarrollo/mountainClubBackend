package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDate;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ClubUser {
  private Long clubId;
  private String nif;
  private String name;
  private String surname;
  private String email;
  private LocalDate birthDate;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  private String phone;
  private String homePhone;
}
