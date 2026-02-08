package luis.delgado.clubmontana.backend.api.exceptions;

public class UsNotFoundException extends RuntimeException {
  public UsNotFoundException(Long clubId) {
    super("US not found for club " + clubId);
  }
}
