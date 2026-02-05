package luis.delgado.clubmontana.backend.end2end.activities;

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
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
public class CreateActivityTest {

  @Autowired private MockMvc mockMvc;
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
  void createActivity_happyPath_returns201() throws Exception {

    Long clubId = insertClub();
    String json =
        """
                   {
                     "title": "Ruta guiada al Pico del Águila",
                     "description": "Actividad de montaña de nivel medio con guía titulado.",
                     "affiliatePrice": 25.00,
                     "noAffiliatePrice": 40.00,
                     "startDate": "2026-03-15T08:30:00",
                     "endDate": "2026-03-15T17:00:00",
                     "images": [
                       {
                         "image": "image-1",
                         "description": "Salida desde el refugio"
                       }
                     ]
                   }
                    """;

    MockPart data = new MockPart("activity", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image =
        new MockMultipartFile(
            "image-1",
            "photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    UtilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(multipart("/clubs/{clubId}/activities", clubId).part(data).file(image))
        .andExpect(status().isCreated());
  }

  @Test
  void createPublication_withoutAuthentication_returns401() throws Exception {
    Long clubId = insertClub();
    MockPart data = new MockPart("data", "{}".getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/clubs/{clubId}/activities", clubId).part(data))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createPublication_invalidPayload_returns400() throws Exception {
    Long clubId = insertClub();
    String invalidJson =
        """
                    {
                      "text": "sin título"
                    }
                    """;

    MockPart data = new MockPart("data", invalidJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    mockMvc
        .perform(
            multipart("/clubs/{clubId}/activities", clubId)
                .part(data)
                .with(authentication(authentication)))
        .andExpect(status().isBadRequest());
  }
}
