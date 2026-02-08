package luis.delgado.clubmontana.backend.end2end.us;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UsEntityJpa;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CreateUsTest {

  private static final Path IMAGES_DIR =
      Paths.get("D:/Proyectos/ClubMontaña/backend/data/test/images");

  @TempDir Path tempDir;
  FileSystemFileStorageService service;
  @Autowired private MockMvc mockMvc;
  @Autowired private UsEntityJpa usEntityJpa;
  @Autowired private UtilTest utilTest;

  @AfterAll
  static void afterAll() throws IOException {
    if (Files.exists(IMAGES_DIR)) {
      // Borrar primero los archivos hijos
      Files.walk(IMAGES_DIR)
          .sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  throw new RuntimeException("Error borrando " + path, e);
                }
              });

      System.out.println("🧹 Carpeta de imágenes de test borrada");
    } else {
      System.out.println("ℹ️ La carpeta no existe, nada que borrar");
    }
  }

  @BeforeEach
  void setUp() {
    service = new FileSystemFileStorageService(tempDir.toString());
  }

  @Test
  void shouldCreateUsWithImagesAndFiles() throws Exception {
    Long clubId = utilTest.insertClub();

    // ---------- multipart files ----------
    MockMultipartFile file1 =
        new MockMultipartFile(
            "files",
            "image1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    // ---------- UsRequestDto como JSON ----------
    String usRequestJson =
        """
        {
          "text": "Texto de prueba",
          "images": [
            { "image": "image1.jpg" }
          ]
        }
        """;

    MockPart data = new MockPart("us", usRequestJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(
            multipart("/clubs/{clubId}/us", clubId)
                .part(data)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());

    Optional<UsEntity> usEntityOptional = usEntityJpa.findById(clubId);
    assertThat(usEntityOptional.isPresent()).isTrue();
    UsEntity usEntity = usEntityOptional.get();
    assertThat(usEntity.getClubId()).isEqualTo(clubId);
    assertThat(usEntity.getText()).isEqualTo("Texto de prueba");

    Optional<UsEntity> images = usEntityJpa.findById(usEntity.getClubId());
    assertThat(images).isPresent();
    assertThat(images.get().getImages()).hasSize(1);
  }
}
