package luis.delgado.clubmontana.backend.domain.model.commands.createClub;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClubRequest {
  String phone;
}
