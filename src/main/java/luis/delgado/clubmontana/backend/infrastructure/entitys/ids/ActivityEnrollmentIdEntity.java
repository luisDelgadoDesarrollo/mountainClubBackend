package luis.delgado.clubmontana.backend.infrastructure.entitys.ids;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ActivityEnrollmentIdEntity implements Serializable {
  private Long activityId;
  private String nif;
}
