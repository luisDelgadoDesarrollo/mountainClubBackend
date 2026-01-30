package luis.delgado.clubmontana.backend.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UsRequest {
  private Long clubId;
  private String text;
  private List<UsImage> images;
}
