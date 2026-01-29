package luis.delgado.clubmontana.backend.domain.model.commands.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
  String accessToken;
  String tokenType;
  Long expiresIn;
  String refreshToken;
}
