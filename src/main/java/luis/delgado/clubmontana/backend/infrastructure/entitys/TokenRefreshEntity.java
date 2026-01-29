package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "tokenRefresh")
@Entity
public class TokenRefreshEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_refresh_id")
  private Long tokenRefreshId;

  @Column(name = "token_hash", nullable = false, unique = true)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "invalid")
  private Boolean invalid;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "club_id")
  private Long clubId;

  @Column(name = "email")
  private String email;
}
