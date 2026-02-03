package luis.delgado.clubmontana.backend.end2end.us;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import luis.delgado.clubmontana.backend.application.services.FileSystemImageStorageService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UpdateUsTest {
  private static final Path IMAGES_DIR =
      Paths.get("D:/Proyectos/ClubMontaña/backend/data/test/images");
  @TempDir Path tempDir;
  FileSystemImageStorageService service;
  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;

  @AfterAll
  static void afterAll() throws IOException {
    if (Files.exists(IMAGES_DIR)) {
      Files.walk(IMAGES_DIR)
          .sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
    }
  }

  @BeforeEach
  void setUp() {
    service = new FileSystemImageStorageService(tempDir.toString());
  }

  // ---------------- CLEANUP ----------------

  // ---------------- HELPERS ----------------
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

  private Long insertUs(Long clubId) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  "INSERT INTO us (club_id, text) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

          String suffix = UUID.randomUUID().toString().substring(0, 8);

          ps.setLong(1, clubId);

          ps.setString(2, "test");

          return ps;
        },
        keyHolder);

    return keyHolder.getKey().longValue();
  }

  // ---------------- TESTS ----------------

  @Test
  void updateUs_happyPath_returns204() throws Exception {

    Long clubId = insertClub();
    insertUs(clubId);

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

    MockMultipartFile image =
        new MockMultipartFile(
            "img-1",
            "photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    mockMvc
        .perform(
            multipart("/us/{clubId}", clubId)
                .part(data)
                .file(image)
                .with(authentication(authentication))
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isNoContent());
  }

  @Test
  void updateUs_withoutAuthentication_returns401() throws Exception {

    Long clubId = insertClub();

    MockMultipartFile usPart =
        new MockMultipartFile(
            "us", "", MediaType.APPLICATION_JSON_VALUE, "{}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/us/{clubId}", clubId)
                .file(usPart)
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void updateUs_invalidPayload_returns400() throws Exception {

    Long clubId = insertClub();

    String invalidJson =
        """
                {
                  "images": []
                }
                """;

    MockPart data = new MockPart("us", invalidJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    mockMvc
        .perform(
            multipart("/us/{clubId}", clubId)
                .part(data)
                .with(authentication(authentication))
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().is4xxClientError());
  }
}
