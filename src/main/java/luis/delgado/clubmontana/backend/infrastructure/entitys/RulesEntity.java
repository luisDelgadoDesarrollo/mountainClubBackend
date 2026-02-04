package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "rules")
@Entity
public class RulesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ruleId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "club_id", nullable = false)
  private ClubEntity club;

  @Column(nullable = false)
  private String rule;
}
