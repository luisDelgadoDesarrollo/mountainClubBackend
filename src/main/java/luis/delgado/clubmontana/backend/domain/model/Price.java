package luis.delgado.clubmontana.backend.domain.model;

import java.math.BigDecimal;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {
  Long clubId;
  String title;
  BigDecimal price;
}
