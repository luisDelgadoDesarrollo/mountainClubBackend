package luis.delgado.clubmontana.backend.unit.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.api.exceptions.UnsupportedImageTypeException;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.model.enums.PdfType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("test")
class FileSystemFileStorageServiceTest {

  @TempDir Path tempDir;

  FileSystemFileStorageService service;

  @BeforeEach
  void setUp() {
    service = new FileSystemFileStorageService(tempDir.toString());
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

    UnsupportedImageTypeException ex =
        assertThrows(
            UnsupportedImageTypeException.class,
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

  @Test
  void getImages_whenDirectoryDoesNotExist_returnsEmptyList() {
    FileSystemFileStorageService service = new FileSystemFileStorageService(tempDir.toString());

    List<String> images = service.getImages(1L, 99L, ImageType.PUBLICATION);

    assertNotNull(images);
    assertTrue(images.isEmpty());
  }

  @Test
  void getImages_whenImagesExist_returnsRelativePathsWithForwardSlashes() throws Exception {

    FileSystemFileStorageService service = new FileSystemFileStorageService(tempDir.toString());

    Path publicationDir =
        tempDir.resolve("club_1").resolve(ImageType.PUBLICATION.name()).resolve("PUBLICATION_10");

    Files.createDirectories(publicationDir);

    Files.createFile(publicationDir.resolve("1.jpg"));
    Files.createFile(publicationDir.resolve("2.png"));

    List<String> images = service.getImages(1L, 10L, ImageType.PUBLICATION);

    assertEquals(2, images.size());

    images.forEach(
        path -> {
          assertTrue(path.startsWith("club_1/"));
          assertTrue(path.contains("/PUBLICATION_10/"));
          assertFalse(path.contains(File.separator.equals("/") ? "\\" : File.separator));
        });
  }

  @Test
  void getImages_ignoresSubdirectories() throws Exception {

    FileSystemFileStorageService service = new FileSystemFileStorageService(tempDir.toString());

    Path publicationDir =
        tempDir.resolve("club_1").resolve(ImageType.PUBLICATION.name()).resolve("publication_20");

    Files.createDirectories(publicationDir);
    Files.createFile(publicationDir.resolve("image.jpg"));

    // subdirectorio (debe ignorarse)
    Files.createDirectories(publicationDir.resolve("thumbnails"));

    List<String> images = service.getImages(1L, 20L, ImageType.PUBLICATION);

    assertEquals(1, images.size());
    assertTrue(images.getFirst().endsWith("image.jpg"));
  }

  @Test
  void getImages_whenPathTraversalAttempt_throwsSecurityException() {

    FileSystemFileStorageService service = new FileSystemFileStorageService(tempDir.toString());

    assertThrows(
        IllegalArgumentException.class, () -> service.getImages(1L, 1L, ImageType.valueOf("..")));
  }

  @Test
  void saveAndGetPdf_happyPath() throws Exception {
    Long clubId = 1L;
    String name = "pylaws";

    byte[] content = "PDF content".getBytes();
    MultipartFile file = new MockMultipartFile("file", "estatutos.pdf", "application/pdf", content);

    service.savePdf(clubId, file, PdfType.BY_LAWS);

    Resource resource = service.getPdf(clubId, PdfType.BY_LAWS);

    assertThat(resource).isNotNull();
    assertThat(resource.exists()).isTrue();
    try (InputStream is = resource.getInputStream()) {
      assertThat(is.readAllBytes()).isEqualTo(content);
    }
  }
}
