package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ActivityEnrollment {
  private Long activityId;
  private String name;
  private String surname;
  private String nif;
  private String email;
  private Boolean paid;
  private LocalDateTime createdAt;
}
