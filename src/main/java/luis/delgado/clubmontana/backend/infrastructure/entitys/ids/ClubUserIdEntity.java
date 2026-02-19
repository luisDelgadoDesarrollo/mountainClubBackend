package luis.delgado.clubmontana.backend.infrastructure.entitys.ids;

import java.io.Serializable;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ClubUserIdEntity implements Serializable {
  private Long clubId;
  private String email;
  private String nif;
}
