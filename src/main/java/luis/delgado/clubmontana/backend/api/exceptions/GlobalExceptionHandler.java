package luis.delgado.clubmontana.backend.api.exceptions;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UnsupportedImageTypeException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE) // 415
  public Map<String, String> handleUnsupportedImageType(UnsupportedImageTypeException ex) {

    return Map.of("error", "UNSUPPORTED_IMAGE_TYPE", "message", ex.getMessage());
  }

  @ExceptionHandler(PublicationNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePublicationNotFound(PublicationNotFoundException ex) {
    return Map.of("error", "PUBLICATION_NOT_FOUND", "message", ex.getMessage());
  }

  @ExceptionHandler(ActivityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePublicationNotFound(ActivityNotFoundException ex) {
    return Map.of("error", "ACTIVITY_NOT_FOUND", "message", ex.getMessage());
  }

  @ExceptionHandler(ArticleNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePublicationNotFound(ArticleNotFoundException ex) {
    return Map.of("error", "ARTICLE_NOT_FOUND", "message", ex.getMessage());
  }

  @ExceptionHandler(PdfStorageException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Map<String, String> handlePdfStorageException(PdfStorageException ex) {
    return Map.of("error", "PDF_STORAGE_EXCEPTION", "message", ex.getMessage());
  }

  @ExceptionHandler(PdfGetException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePdfGetException(PdfGetException ex) {
    return Map.of("error", "PDF_GET_EXCEPTION", "message", ex.getMessage());
  }

  @ExceptionHandler(ImageNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePdfGetException(ImageNotFoundException ex) {
    return Map.of("error", "IMAGE_GET_EXCEPTION", "message", ex.getMessage());
  }

  @ExceptionHandler(UsNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePdfGetException(UsNotFoundException ex) {
    return Map.of("error", "US_GET_EXCEPTION", "message", ex.getMessage());
  }

  @ExceptionHandler(SlugNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handlePdfGetException(SlugNotFoundException ex) {
    return Map.of("error", "SLUG_NOT_FOUND_EXCEPTION", "message", ex.getMessage());
  }

  @ExceptionHandler(BadDateActivity.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handlePdfGetException(BadDateActivity ex) {
    return Map.of("error", "ACTIVITY_DATE_EXCEPTION", "message", ex.getMessage());
  }

  @ExceptionHandler(ClubUserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handleClubUserNotFoundException(ClubUserNotFoundException ex) {
    return Map.of("error", "CLUB_USER_NOT_FOUND", "message", ex.getMessage());
  }
}
