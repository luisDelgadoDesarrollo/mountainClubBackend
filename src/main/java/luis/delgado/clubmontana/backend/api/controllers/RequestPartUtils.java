package luis.delgado.clubmontana.backend.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RequestPartUtils {

  private final ObjectMapper objectMapper;
  private final Validator validator;

  public RequestPartUtils(ObjectMapper objectMapper, Validator validator) {
    this.objectMapper = objectMapper;
    this.validator = validator;
  }

  public <T> T parseAndValidate(String data, Class<T> clazz) {
    T request;
    try {
      request = objectMapper.readValue(data, clazz);
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data payload", ex);
    }
    Set<ConstraintViolation<T>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data payload");
    }
    return request;
  }

  public Map<String, MultipartFile> toFileMap(List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      return Map.of();
    }
    return files.stream()
        .filter(file -> !file.isEmpty())
        .collect(
            Collectors.toMap(
                file -> {
                  String filename = file.getOriginalFilename();
                  if (filename == null || filename.isBlank()) {
                    return file.getName();
                  }
                  return filename;
                },
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new));
  }
}
