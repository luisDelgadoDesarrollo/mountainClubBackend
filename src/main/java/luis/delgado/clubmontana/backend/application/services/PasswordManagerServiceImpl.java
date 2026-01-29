package luis.delgado.clubmontana.backend.application.services;

import luis.delgado.clubmontana.backend.domain.services.PasswordManagerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordManagerServiceImpl implements PasswordManagerService {
  private final PasswordEncoder passwordEncoder;

  public PasswordManagerServiceImpl(PasswordEncoder passwordEncoder) {
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
