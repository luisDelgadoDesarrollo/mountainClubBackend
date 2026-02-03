package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "us")
@Entity
public class UsEntity {

  @Id
  @Column(name = "club_id")
  private Long clubId;

  @OneToOne
  @MapsId
  @JoinColumn(name = "club_id", insertable = false, updatable = false)
  private ClubEntity club;

  @Column(columnDefinition = "text")
  private String text;

  @OneToMany(mappedBy = "us", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UsImageEntity> images = new ArrayList<>();
}
