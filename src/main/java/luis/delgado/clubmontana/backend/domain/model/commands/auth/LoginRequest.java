package luis.delgado.clubmontana.backend.domain.model.commands.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
  String username;
  String password;
}
