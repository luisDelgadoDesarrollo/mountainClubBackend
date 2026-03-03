package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "price")
public class PriceEntity {

  private static final BigDecimal DEFAULT_PRICE = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "price_id")
  private Long priceId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "club_id", nullable = false)
  private ClubEntity club;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  public void setPrice(BigDecimal price) {
    this.price = normalizePrice(price);
  }

  @PrePersist
  @PreUpdate
  protected void normalizeFields() {
    this.price = normalizePrice(this.price);
  }

  private BigDecimal normalizePrice(BigDecimal value) {
    if (value == null) return DEFAULT_PRICE;
    return value.setScale(2, RoundingMode.HALF_UP);
  }
}
