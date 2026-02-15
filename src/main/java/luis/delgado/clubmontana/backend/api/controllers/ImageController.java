package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

  private final FileSystemFileStorageService fileStorageService;

  public ImageController(FileSystemFileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @GetMapping("/**")
  public ResponseEntity<Resource> get(HttpServletRequest request) {

    String relativePath = request.getRequestURI().replace("/images/", "");

    Resource image = fileStorageService.loadImage(relativePath);

    return ResponseEntity.ok()
        .contentType(
            MediaTypeFactory.getMediaType(relativePath).orElse(MediaType.APPLICATION_OCTET_STREAM))
        .header(HttpHeaders.CACHE_CONTROL, "no-store, max-age=0")
        .header(HttpHeaders.PRAGMA, "no-cache")
        .header(HttpHeaders.EXPIRES, "0")
        .body(image);
  }
}
