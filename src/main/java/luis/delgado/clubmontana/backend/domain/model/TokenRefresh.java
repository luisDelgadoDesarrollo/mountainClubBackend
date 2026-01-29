package luis.delgado.clubmontana.backend.domain.model;

import java.time.LocalDateTime;

public record TokenRefresh(
    Long tokenRefreshId,
    String token,
    LocalDateTime expiresAt,
    Boolean invalid,
    Long userId,
    Long clubId,
    String email) {

  public TokenRefresh(
      String token, LocalDateTime expiresAt, Long userId, Long clubId, String email) {
    this(null, token, expiresAt, false, userId, clubId, email);
  }

  public TokenRefresh invalidate() {
    return new TokenRefresh(
        this.tokenRefreshId,
        this.token,
        this.expiresAt,
        true,
        this.userId,
        this.clubId,
        this.email);
  }
}
