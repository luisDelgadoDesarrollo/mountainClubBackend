package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UpdatePublicationTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void updatePublication_happyPath_returns201() throws Exception {

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> publication = utilTest.createPublication(club);

    String json =
        """
            {
              "title": "Updated title",
              "text": "Updated text",
              "images": [
                { "image": "photo.jpg", "description": "desc" }
              ],
              "links": []
            }
            """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "photo.jpg",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(club.id());

    // when / then
    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/publications/{publication}",
                    club.slug(),
                    publication.getSecond())
                .part(data)
                .file(image))
        .andExpect(status().isCreated());
  }

  @Test
  void updatePublication_withoutAuthentication() throws Exception {

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> publication = utilTest.createPublication(club);

    String json =
        """
                {
                  "title": "Updated title",
                  "text": "Updated text",
                  "images": [
                    { "image": "photo.jpg", "description": "desc" }
                  ],
                  "links": []
                }
                """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "photo.jpg",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    // when / then
    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/publications/{publication}",
                    club.slug(),
                    publication.getSecond())
                .part(data)
                .file(image)
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void updatePublication_badPayload() throws Exception {

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> publication = utilTest.createPublication(club);

    String json =
        """
                {
                  "text": "json incorrecto"
                }
                """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "photo.jpg",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(club.id());

    // when / then
    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/publications/{publication}",
                    club.slug(),
                    publication.getSecond())
                .part(data)
                .file(image))
        .andExpect(status().is4xxClientError());
  }
}
