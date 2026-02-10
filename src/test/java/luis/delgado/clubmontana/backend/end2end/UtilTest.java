package luis.delgado.clubmontana.backend.end2end;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;
import luis.delgado.clubmontana.backend.domain.model.CustomUserDetails;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class UtilTest {

  private final JdbcTemplate jdbcTemplate;
  private final MockMvc mockMvc;
  private final Path tempDir;

  public UtilTest(JdbcTemplate jdbcTemplate, MockMvc mockMvc, Path tempDir) {
    this.jdbcTemplate = jdbcTemplate;
    this.mockMvc = mockMvc;
    this.tempDir = tempDir;
  }

  public void mockUserWithClub(Long clubId) {
    CustomUserDetails user = new CustomUserDetails("email@test", 1L, clubId);

    Authentication auth =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public Long insertClub() {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  """
                                                                INSERT INTO club (
                                                                  name,
                                                                  slug,
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
                                                                  has_hazte_socio,
                                                                contact_email
                                                                ) VALUES (
                                                                  ?,? ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,
                                                                  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                                                                )
                                                                """,
                  Statement.RETURN_GENERATED_KEYS);

          String suffix = UUID.randomUUID().toString().substring(0, 8);

          ps.setString(1, "Club Test");
          ps.setString(2, "club-test-" + suffix);

          ps.setString(3, "G" + suffix); // nif único
          ps.setString(4, "Club de prueba");
          ps.setString(5, "logo.png");
          ps.setString(6, "club-" + suffix + ".es"); // url única
          ps.setLong(7, 1L); // created_by

          ps.setBoolean(8, true); // has_inicio
          ps.setBoolean(9, true); // has_secciones
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
          ps.setBoolean(21, false);
          ps.setString(22, "delgadofernandez.luis@gmail.com");

          return ps;
        },
        keyHolder);

    return keyHolder.getKey().longValue();
  }

  public Long createActivity(Long clubId) throws Exception {

    String json =
        """
                             {
                               "title": "Ruta guiada al Pico del Águila",
                               "description": "Actividad de montaña de nivel medio con guía titulado.",
                               "affiliatePrice": 25.00,
                               "startDate": "2026-03-15T08:30:00",
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

    mockUserWithClub(clubId);
    MvcResult mvcResult =
        mockMvc
            .perform(multipart("/clubs/{clubId}/activities", clubId).part(data).file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    return Long.valueOf(id);
  }

  public void createImage(Long ownerId, Long clubId, String filename) throws IOException {
    Path dir =
        tempDir
            .resolve("club_" + clubId)
            .resolve(ImageType.ACTIVITY.name())
            .resolve("activity_" + ownerId);

    Files.createDirectories(dir);
    Files.createFile(dir.resolve(filename));
  }

  public Long createArticle(Long clubId) throws Exception {
    String articleJson =
        """
                  {
                    "title": "Artículo integración",
                    "description": "Descripción test",
                    "images": [
                      { "image": "image-1" }
                    ],
                    "variants": [
                      {
                        "size": "M",
                        "color": "Rojo",
                        "stock": 5,
                        "images": [
                          { "image": "image-2" }
                        ]
                      }
                    ]
                  }
                  """;

    MockPart data = new MockPart("article", articleJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image1 =
        new MockMultipartFile(
            "image-1",
            "image-1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    MockMultipartFile image2 =
        new MockMultipartFile(
            "image-2",
            "image-2.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockUserWithClub(clubId);
    MvcResult mvcResult =
        mockMvc
            .perform(
                multipart("/clubs/{clubId}/articles", clubId).part(data).file(image1).file(image2))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    return Long.valueOf(id);
  }

  public Long createPublication(Long clubId) throws Exception {
    String json =
        """
                  {
                    "title": "Mi publicación",
                    "text": "Texto",
                    "images": [
                      { "image": "img-1", "description": "foto 1" }
                    ],
                    "links": []
                  }
                  """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image =
        new MockMultipartFile(
            "img-1",
            "photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockUserWithClub(clubId);
    MvcResult mvcResult =
        mockMvc
            .perform(multipart("/clubs/{clubId}/publications", clubId).part(data).file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    return Long.valueOf(id);
  }
}
