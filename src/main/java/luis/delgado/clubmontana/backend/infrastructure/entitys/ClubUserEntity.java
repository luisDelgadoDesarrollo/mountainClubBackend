package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ClubUserIdEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "club_user")
@IdClass(ClubUserIdEntity.class)
public class ClubUserEntity {

  @Id
  @Column(name = "club_id", nullable = false)
  private Long clubId;

  @Id
  @Column(name = "email", nullable = false, length = 255)
  private String email;

  @Id
  @Column(name = "nif", nullable = false, length = 50)
  private String nif;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "surname", nullable = false, length = 255)
  private String surname;

  @Column(name = "federateNumber", nullable = true, length = 50)
  private String federatedNumber;

  @Column(nullable = false)
  private LocalDate birthDate;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String state;

  @Column(nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String homePhone;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "club_id",
      referencedColumnName = "club_id",
      insertable = false,
      updatable = false)
  private ClubEntity club;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
