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

  @Column(name = "nif", nullable = false, length = 50)
  private String nif;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column private String surname;

  @Column private LocalDate birthDate;
  @Column private String address;
  @Column private String city;
  @Column private String state;
  @Column private String postalCode;
  @Column private String phone;
  @Column private String homePhone;
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

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
