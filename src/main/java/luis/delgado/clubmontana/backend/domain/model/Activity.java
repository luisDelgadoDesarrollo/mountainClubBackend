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
  private Long activityId;
  private String slug;
  private Long clubId;
  private String title;
  private String description;
  private BigDecimal affiliatePrice;
  private BigDecimal noAffiliatePrice;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer maxParticipants;
  private List<Image> images;
}
