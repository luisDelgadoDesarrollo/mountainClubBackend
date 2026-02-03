package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UsImage {
  private Long clubId;
  private String image;
  private String description;
}
