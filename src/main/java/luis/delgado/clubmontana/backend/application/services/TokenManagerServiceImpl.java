package luis.delgado.clubmontana.backend.application.services;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.domain.model.commands.auth.TokenResponse;
import luis.delgado.clubmontana.backend.domain.repository.AuthRepository;
import luis.delgado.clubmontana.backend.domain.services.TokenManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenManagerServiceImpl implements TokenManagerService {

  private final SecretKey secretKey;
  private final long expirationSeconds;
  private final AuthRepository authRepository;

  public TokenManagerServiceImpl(
      SecretKey secretKey,
      @Value("${security.jwt.expiration-seconds}") long expirationSeconds,
      AuthRepository authRepository) {
    this.secretKey = secretKey;
    this.expirationSeconds = expirationSeconds;
    this.authRepository = authRepository;
  }

  @Override
  public TokenResponse generate(Long userId, Long clubId, String email) {

    Instant now = Instant.now();

    String accessToken =
        Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .claim("clubId", clubId)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expirationSeconds)))
            .signWith(secretKey)
            .compact();
    String uuid = UUID.randomUUID().toString();
    authRepository.saveTokenRefresh(
        new TokenRefresh(hashToken(uuid), LocalDateTime.now().plusDays(3), userId, clubId, email));
    return new TokenResponse(accessToken, "Bearer", expirationSeconds, uuid);
  }

  @Override
  public String hashToken(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 algorithm not available", e);
    }
  }

  @Override
  public Boolean matchHashToken(String rawToken, String hashToken) {
    if (rawToken == null || hashToken == null) {
      return false;
    }
    return hashToken(rawToken).equals(hashToken);
  }
}
