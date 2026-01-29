package luis.delgado.clubmontana.backend.domain.model.commands.createPublication;

import java.util.List;
import lombok.*;
import luis.delgado.clubmontana.backend.domain.model.PublicationImage;
import luis.delgado.clubmontana.backend.domain.model.PublicationLink;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePublicationRequest {
  String title;
  String text;
  List<PublicationImage> images;
  List<PublicationLink> links;
}
