package luis.delgado.clubmontana.backend.api.exceptions;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException(Long publicationId, Long clubId) {
    super("Article " + publicationId + " not found for club " + clubId);
  }
}
