package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PublicationImage {
  Long publicationImageId;
  Long publicationId;
  String image;
  String description;
}
