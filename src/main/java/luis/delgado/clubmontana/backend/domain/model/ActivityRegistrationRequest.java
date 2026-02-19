package luis.delgado.clubmontana.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ActivityRegistrationRequest {
  String name;
  String surname;
  String nif;
  String email;
  String federateNumber;
}
