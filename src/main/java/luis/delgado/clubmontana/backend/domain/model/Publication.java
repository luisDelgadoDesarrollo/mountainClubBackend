package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Publication {
  Long publicationId;
  Long clubId;
  String title;
  String text;
  List<PublicationImage> images;
  List<PublicationLink> links;
  LocalDateTime createdAt;
}
