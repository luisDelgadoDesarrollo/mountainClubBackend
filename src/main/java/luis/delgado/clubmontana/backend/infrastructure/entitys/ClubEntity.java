package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "club")
public class ClubEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "club_id")
  private Long clubId;

  @Column(name = "slug", nullable = false, updatable = false, unique = true)
  private String slug;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, unique = true, length = 50)
  private String nif;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 255)
  private String logo;

  @Column(nullable = false, unique = true)
  private String url;

  @Column(name = "contact_email", nullable = false)
  private String contactEmail;

  @Column(name = "phone", length = 255)
  private String phone;

  @Column(name = "iban", length = 34)
  private String iban;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(nullable = false)
  private boolean hasInicio;

  @Column(nullable = false)
  private boolean hasSecciones;

  @Column(nullable = false)
  private boolean hasGaleria;

  @Column(nullable = false)
  private boolean hasEnlaces;

  @Column(nullable = false)
  private boolean hasContacto;

  @Column(nullable = false)
  private boolean hasFederarse;

  @Column(nullable = false)
  private boolean hasTienda;

  @Column(nullable = false)
  private boolean hasCalendario;

  @Column(nullable = false)
  private boolean hasConocenos;

  @Column(nullable = false)
  private boolean hasNoticias;

  @Column(nullable = false)
  private boolean hasForo;

  @Column(nullable = false)
  private boolean hasEstatutos;

  @Column(nullable = false)
  private boolean hasNormas;

  @Column(nullable = false)
  private boolean hasHazteSocio;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
