package luis.delgado.clubmontana.backend.unit.services;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import luis.delgado.clubmontana.backend.application.services.FileSystemImageStorageService;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class FileSystemImageStorageServiceTest {

  @TempDir Path tempDir;

  FileSystemImageStorageService service;

  @BeforeEach
  void setUp() {
    service = new FileSystemImageStorageService(tempDir.toString());
  }

  private byte[] minimalJpeg() {
    return new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
  }

  private byte[] minimalPng() {
    return new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
  }

  private byte[] minimalWebp() {
    return "RIFFxxxxWEBP".getBytes();
  }

  @Test
  void store_validJpegImage_storesFileInExpectedLocation() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("img-1", "photo.jpg", "image/jpeg", minimalJpeg());

    service.store(Map.of("img-1", file), Map.of("img-1", 10L), 5L, 2L, ImageType.PUBLICATION);

    Path expected =
        tempDir.resolve("club_2").resolve("PUBLICATION").resolve("publication_5").resolve("10.jpg");

    assertTrue(Files.exists(expected));
  }

  @Test
  void store_validPngImage_storesFileInExpectedLocation() throws Exception {
    MockMultipartFile file = new MockMultipartFile("img-1", "photo.png", "image/png", minimalPng());

    service.store(Map.of("img-1", file), Map.of("img-1", 1L), 1L, 1L, ImageType.PUBLICATION);

    Path expected =
        tempDir.resolve("club_1").resolve("PUBLICATION").resolve("publication_1").resolve("1.png");

    assertTrue(Files.exists(expected));
  }

  @Test
  void store_filesAndImageIdsSizeMismatch_throwsIllegalArgumentException() {
    MockMultipartFile file =
        new MockMultipartFile("img-1", "photo.jpg", "image/jpeg", minimalJpeg());

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.store(Map.of("img-1", file), Map.of(), 1L, 1L, ImageType.PUBLICATION));

    assertEquals("Files count and imageIds count must match", ex.getMessage());
  }

  @Test
  void store_unsupportedMimeType_throwsIllegalArgumentException() {
    MockMultipartFile file =
        new MockMultipartFile("img-1", "file.txt", "text/plain", "hello".getBytes());

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                service.store(
                    Map.of("img-1", file), Map.of("img-1", 1L), 1L, 1L, ImageType.PUBLICATION));

    assertTrue(ex.getMessage().contains("Unsupported image type"));
  }

  @Test
  void store_createsDirectoriesIfTheyDoNotExist() {
    MockMultipartFile file =
        new MockMultipartFile("img-1", "photo.webp", "image/webp", minimalWebp());

    service.store(Map.of("img-1", file), Map.of("img-1", 99L), 10L, 20L, ImageType.PUBLICATION);

    Path publicationDir =
        tempDir.resolve("club_20").resolve("PUBLICATION").resolve("publication_10");

    assertTrue(Files.exists(publicationDir));
    assertTrue(Files.isDirectory(publicationDir));
  }

  @Test
  void deleteImages_existingDirectoryWithContent_deletesEverything() throws Exception {
    // given
    MockMultipartFile file =
        new MockMultipartFile("img-1", "photo.jpg", "image/jpeg", minimalJpeg());

    service.store(Map.of("img-1", file), Map.of("img-1", 1L), 10L, 20L, ImageType.PUBLICATION);

    Path publicationDir =
        tempDir.resolve("club_20").resolve("PUBLICATION").resolve("publication_10");

    assertTrue(Files.exists(publicationDir));
    assertFalse(Files.list(publicationDir).toList().isEmpty());

    // when
    service.deleteImages(20L, ImageType.PUBLICATION, 10L);

    // then
    assertFalse(Files.exists(publicationDir));
  }

  @Test
  void deleteImages_directoryDoesNotExist_doesNothing() {
    // when / then
    assertDoesNotThrow(() -> service.deleteImages(99L, ImageType.PUBLICATION, 999L));
  }

  @Test
  void deleteImages_deletesNestedDirectoriesAndFiles() throws Exception {
    // given
    Path publicationDir = tempDir.resolve("club_1").resolve("PUBLICATION").resolve("publication_1");

    Path nestedDir = publicationDir.resolve("nested");
    Files.createDirectories(nestedDir);

    Files.write(nestedDir.resolve("a.jpg"), minimalJpeg());
    Files.write(publicationDir.resolve("b.jpg"), minimalJpeg());

    assertTrue(Files.exists(nestedDir.resolve("a.jpg")));
    assertTrue(Files.exists(publicationDir.resolve("b.jpg")));

    // when
    service.deleteImages(1L, ImageType.PUBLICATION, 1L);

    // then
    assertFalse(Files.exists(publicationDir));
  }
}
