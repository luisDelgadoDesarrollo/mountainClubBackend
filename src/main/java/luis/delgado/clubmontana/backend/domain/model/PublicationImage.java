package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PublicationImage {
  Long publicationId;
  Long imageId;
  String image;
  String desc;
}
