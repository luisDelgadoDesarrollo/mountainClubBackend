package luis.delgado.clubmontana.backend.application.services;

import luis.delgado.clubmontana.backend.domain.services.PasswordManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordManagerImpl implements PasswordManager {
  private final PasswordEncoder passwordEncoder;

  public PasswordManagerImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String hash(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  @Override
  public boolean matches(String hashedPassword, String rawPassword) {
    return passwordEncoder.matches(hashedPassword, rawPassword);
  }
}
