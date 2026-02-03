package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UpdatePublicationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private PublicationRepository publicationRepository;
  @Autowired private JdbcTemplate jdbcTemplate;

  @AfterAll
  static void afterAll() throws IOException {
    Path dir = Paths.get("D:/Proyectos/ClubMontaña/backend/data/test/images");

    if (Files.exists(dir)) {
      // Borrar primero los archivos hijos
      Files.walk(dir)
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
          ps.setBoolean(15, false);
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
  void updatePublication_happyPath_returns201() throws Exception {

    Long clubId = insertClub();
    Publication publication = new Publication();
    publication.setClubId(clubId);
    publication.setTitle("Original title");
    publication.setText("Original text");
    publication.setImages(List.of());
    publication.setLinks(List.of());

    Publication savedPublication = publicationRepository.savePublication(publication);
    Long publicationId = savedPublication.getPublicationId();

    String json =
        """
            {
              "title": "Updated title",
              "text": "Updated text",
              "images": [
                { "image": "image-1", "description": "desc" }
              ],
              "links": []
            }
            """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "image-1",
            "photo.jpg",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    // when / then
    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT, "/publications/{clubId}/{publicationId}", clubId, publicationId)
                .part(data)
                .file(image)
                .with(authentication(authentication)))
        .andExpect(status().isCreated());
  }
}
