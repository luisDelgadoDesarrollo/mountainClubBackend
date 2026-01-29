package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PublicationLink {
  Long publicationId;
  Long linkId;
  String title;
  String link;
}
