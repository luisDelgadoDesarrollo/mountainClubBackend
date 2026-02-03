package luis.delgado.clubmontana.backend.domain.model;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Us {
  private Long clubId;
  private String text;
  private List<UsImage> images;
}
