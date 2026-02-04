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
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
  @Autowired private JdbcTemplate jdbcTemplate;

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

  private Long insertClub() {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  """
                                              INSERT INTO club (
                                                name,
                                                nif,
                                                description,
                                                logo,
                                                url,
                                                created_at,
                                                created_by,
                                                has_inicio,
                                                has_secciones,
                                                has_galeria,
                                                has_enlaces,
                                                has_contacto,
                                                has_federarse,
                                                has_tienda,
                                                has_calendario,
                                                has_conocenos,
                                                has_noticias,
                                                has_foro,
                                                has_estatutos,
                                                has_normas,
                                                has_hazte_socio
                                              ) VALUES (
                                                ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,
                                                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                                              )
                                              """,
                  Statement.RETURN_GENERATED_KEYS);

          String suffix = UUID.randomUUID().toString().substring(0, 8);

          ps.setString(1, "Club Test");
          ps.setString(2, "G" + suffix); // nif único
          ps.setString(3, "Club de prueba");
          ps.setString(4, "logo.png");
          ps.setString(5, "club-" + suffix + ".es"); // url única
          ps.setLong(6, 1L); // created_by

          ps.setBoolean(7, true); // has_inicio
          ps.setBoolean(8, true); // has_secciones
          ps.setBoolean(9, false);
          ps.setBoolean(10, false);
          ps.setBoolean(11, false);
          ps.setBoolean(12, false);
          ps.setBoolean(13, false);
          ps.setBoolean(14, false);
          ps.setBoolean(15, true);
          ps.setBoolean(16, false);
          ps.setBoolean(17, false);
          ps.setBoolean(18, false);
          ps.setBoolean(19, false);
          ps.setBoolean(20, false);

          return ps;
        },
        keyHolder);

    return keyHolder.getKey().longValue();
  }

  @Test
  void shouldCreateUsWithImagesAndFiles() throws Exception {
    Long clubId = insertClub();

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

    UtilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(
            multipart("/us/{clubId}", clubId)
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
