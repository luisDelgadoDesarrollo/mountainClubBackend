package luis.delgado.clubmontana.backend.domain.services;

public interface PasswordManagerService {
  String hash(String rawPassword);

  boolean matches(String hashedPassword, String rawPassword);
}
