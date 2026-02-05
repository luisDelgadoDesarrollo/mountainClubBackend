package luis.delgado.clubmontana.backend.api.exceptions;

public class ActivityNotFoundException extends RuntimeException {
  public ActivityNotFoundException(Long clubId, Long publicationId) {
    super("Activity " + publicationId + " not found for club " + clubId);
  }
}
