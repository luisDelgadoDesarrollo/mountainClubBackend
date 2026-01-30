package luis.delgado.clubmontana.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UsImage {
  private Long clubId;
  private String image;
  private String description;
}
