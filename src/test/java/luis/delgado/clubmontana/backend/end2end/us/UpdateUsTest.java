package luis.delgado.clubmontana.backend.end2end.us;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
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
class UpdateUsTest extends AbstractWebIntegrationTest {
  FileSystemFileStorageService service;
  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;

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

    ClubInserted club = utilTest.insertClub();
    insertUs(club.id());

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

    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(
            multipart("/clubs/{club}/us", club.slug())
                .part(data)
                .file(image)
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isNoContent());
  }

  @Test
  void updateUs_withoutAuthentication_returns401() throws Exception {

    ClubInserted club = utilTest.insertClub();

    MockMultipartFile usPart =
        new MockMultipartFile(
            "us", "", MediaType.APPLICATION_JSON_VALUE, "{}".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/clubs/{club}/us", club.slug())
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

    ClubInserted club = utilTest.insertClub();

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
            multipart("/clubs/{club}/us", club.slug())
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
