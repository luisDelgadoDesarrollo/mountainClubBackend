package luis.delgado.clubmontana.backend.api.exceptions;

public class PublicationNotFoundException extends RuntimeException {
  public PublicationNotFoundException(Long clubId, Long publicationId) {
    super("Publication " + publicationId + " not found for club " + clubId);
  }
}
