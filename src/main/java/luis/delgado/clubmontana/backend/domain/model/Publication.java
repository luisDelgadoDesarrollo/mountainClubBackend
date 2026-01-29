package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDateTime;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Publication {
  Long publicationId;
  String title;
  String text;
  Long clubId;
  LocalDateTime createdAt;
}
