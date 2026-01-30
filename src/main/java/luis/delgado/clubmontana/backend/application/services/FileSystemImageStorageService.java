package luis.delgado.clubmontana.backend.application.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.services.ImageStorageService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemImageStorageService implements ImageStorageService {

  private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

  private final Path basePath;
  private final Tika tika = new Tika();

  public FileSystemImageStorageService(@Value("${storage.images.base-path}") String basePath) {
    this.basePath = Paths.get(basePath);
  }

  @Override
  public void store(
      Map<String, MultipartFile> files,
      Map<String, Long> imageIds,
      Long publicationId,
      Long clubId,
      ImageType imageType) {

    if (files.size() != imageIds.size()) {
      throw new IllegalArgumentException("Files count and imageIds count must match");
    }

    try {
      Files.createDirectories(basePath);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot create image directory", e);
    }

    files.forEach(
        (key, value) -> storeSingle(value, imageIds.get(key), publicationId, clubId, imageType));
  }

  private void storeSingle(
      MultipartFile file, Long imageId, Long publicationId, Long clubId, ImageType imageType) {

    try (InputStream is = file.getInputStream()) {

      String detectedType = tika.detect(is);

      if (!ALLOWED_TYPES.contains(detectedType)) {
        throw new IllegalArgumentException("Unsupported image type: " + detectedType);
      }

      String extension = extensionFromMime(detectedType);

      Path clubDir = basePath.resolve("club_" + clubId);

      Path typeDir = clubDir.resolve(imageType.name());

      Path publicationDir = typeDir.resolve("publication_" + publicationId);

      Files.createDirectories(publicationDir);

      String filename = imageId + "." + extension;

      Path target = publicationDir.resolve(filename);

      Files.copy(file.getInputStream(), target);

    } catch (IOException e) {
      throw new IllegalStateException("Failed to store image " + imageId, e);
    }
  }

  private String extensionFromMime(String mime) {
    return switch (mime) {
      case "image/jpeg" -> "jpg";
      case "image/png" -> "png";
      case "image/webp" -> "webp";
      default -> throw new IllegalStateException("Unexpected mime: " + mime);
    };
  }
}
