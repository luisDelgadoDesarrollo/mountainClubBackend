package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ActivityEnrollmentIdEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "activity_enrollment")
@IdClass(ActivityEnrollmentIdEntity.class)
public class ActivityEnrollmentEntity {

  @Id
  @Column(name = "activity_id", nullable = false)
  private Long activityId;

  @Id
  @Column(name = "nif", nullable = false, length = 50)
  private String nif;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "surname", nullable = false, length = 255)
  private String surname;

  @Column(name = "email", nullable = false, length = 255)
  private String email;

  @Column(name = "paid", nullable = false)
  private Boolean paid;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "activity_id",
      referencedColumnName = "activity_id",
      insertable = false,
      updatable = false)
  private ActivityEntity activity;

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
    if (paid == null) {
      paid = Boolean.FALSE;
    }
  }
}
