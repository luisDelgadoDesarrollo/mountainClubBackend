package luis.delgado.clubmontana.backend.api.exceptions;

public class ClubUserNotFoundException extends RuntimeException {
  public ClubUserNotFoundException(Long clubId, String email) {
    super("Email: " + email + " no encontrado para el club:  " + clubId);
  }
}
