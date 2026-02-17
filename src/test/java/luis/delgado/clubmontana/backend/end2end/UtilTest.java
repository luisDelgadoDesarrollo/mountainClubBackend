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
import org.springframework.data.util.Pair;
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

  public ClubInserted insertClub() {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String suffix = UUID.randomUUID().toString().substring(0, 8);
    String slug = "club-test-" + suffix;
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  """
                                    INSERT INTO club (
                                      name, slug, nif, description, logo, url,
                                      created_at, created_by,
                                      has_inicio, has_secciones, has_galeria, has_enlaces,
                                      has_contacto, has_federarse, has_tienda, has_calendario,
                                      has_conocenos, has_noticias, has_foro, has_estatutos,
                                      has_normas, has_hazte_socio, contact_email, phone
                                    ) VALUES (
                                      ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,
                                      ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                                    )
                                    """,
                  Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, "Club Test");
          ps.setString(2, slug);
          ps.setString(3, "G" + suffix);
          ps.setString(4, "Club de prueba");
          ps.setString(5, "logo.png");
          ps.setString(6, "club-" + suffix + ".es");
          ps.setLong(7, 1L);
          ps.setBoolean(8, true);
          ps.setBoolean(9, true);
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
          ps.setString(23, "+34600123123");
          return ps;
        },
        keyHolder);
    return new ClubInserted(keyHolder.getKey().longValue(), slug);
  }

  public Pair<Long, String> createActivity(ClubInserted club) throws Exception {

    String json =
        """
                             {
                               "title": "Ruta guiada al Pico del Águila",
                               "description": "Actividad de montaña de nivel medio con guía titulado.",
                               "affiliatePrice": 25.00,
                               "startDate": "2026-01-15T08:30:00",
                               "images": [
                                 {
                                   "image": "image-1",
                                   "description": "Salida desde el refugio"
                                 }
                               ]
                             }
                              """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "image-1",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockUserWithClub(club.id());
    MvcResult mvcResult =
        mockMvc
            .perform(multipart("/clubs/{club}/activities", club.slug()).part(data).file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    String slug = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.slug");
    return Pair.of(Long.valueOf(id), slug);
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

  public Pair<Long, String> createArticle(ClubInserted club) throws Exception {
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

    mockUserWithClub(club.id());
    MvcResult mvcResult =
        mockMvc
            .perform(
                multipart("/clubs/{club}/articles", club.slug())
                    .part(data)
                    .file(image1)
                    .file(image2))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    String slug = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.slug");
    return Pair.of(Long.valueOf(id), slug);
  }

  public Pair<Long, String> createPublication(ClubInserted club) throws Exception {
    String json =
        """
                  {
                    "title": "Mi publicación",
                    "text": "Texto",
                    "images": [
                      { "image": "photo.jpg", "description": "foto 1" }
                    ],
                    "links": []
                  }
                  """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockUserWithClub(club.id());
    MvcResult mvcResult =
        mockMvc
            .perform(multipart("/clubs/{club}/publications", club.slug()).part(data).file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    String slug = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.slug");
    return Pair.of(Long.valueOf(id), slug);
  }

  public void createUs(ClubInserted club) throws Exception {

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

    mockUserWithClub(club.id());

    mockMvc.perform(
        multipart("/clubs/{club}/us", club.slug())
            .part(data)
            .file(file1)
            .contentType(MediaType.MULTIPART_FORM_DATA));
  }
}
