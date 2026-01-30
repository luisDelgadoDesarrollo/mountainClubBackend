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
}
