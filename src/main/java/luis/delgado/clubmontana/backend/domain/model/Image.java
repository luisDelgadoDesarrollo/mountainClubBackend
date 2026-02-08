package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Image {
  Long imageId;
  Long parentId;
  String image;
  String desc;
}
