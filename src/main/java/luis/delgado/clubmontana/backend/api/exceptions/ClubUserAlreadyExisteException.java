package luis.delgado.clubmontana.backend.api.exceptions;

public class ClubUserAlreadyExisteException extends RuntimeException {
  public ClubUserAlreadyExisteException(Long clubId, String email) {
    super("Email: " + email + " ya existe para el club:  " + clubId);
  }
}
