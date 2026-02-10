package luis.delgado.clubmontana.backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Activity {
  Long activityId;
  String slug;
  Long clubId;
  String title;
  String description;
  BigDecimal affiliatePrice;
  BigDecimal noAffiliatePrice;
  LocalDateTime startDate;
  LocalDateTime endDate;
  List<Image> images;
}
