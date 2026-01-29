package luis.delgado.clubmontana.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserToLogin {
  private Long userId;
  private String email;
  private Boolean emailVerified;
  private String password;
  private Long clubId;
}
