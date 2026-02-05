package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;
import luis.delgado.clubmontana.backend.domain.model.enums.Moment;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ActivityImage {
  Long activityImageId;
  Long activityId;
  String image;
  Moment moment;
}
