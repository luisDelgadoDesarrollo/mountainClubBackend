package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
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
@ActiveProfiles("test")
class CreatePublicationTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void createPublication_happyPath_returns201() throws Exception {

    Long clubId = utilTest.insertClub();
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

    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(multipart("/clubs/{clubId}/publications", clubId).part(data).file(image))
        .andExpect(status().isCreated());
  }

  @Test
  void createPublication_withoutAuthentication_returns401() throws Exception {
    Long clubId = utilTest.insertClub();
    MockPart data =
        new MockPart(
            "data",
"""
            {
              "title": "Mi publicación",
              "text": "Texto",
              "images": [
                { "image": "img-1", "description": "foto 1" }
              ],
              "links": []
            }
"""
                .getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/clubs/{clubId}/publications", clubId).part(data))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createPublication_invalidPayload_returns400() throws Exception {
    Long clubId = utilTest.insertClub();
    String invalidJson =
        """
            {
              "text": "sin título"
            }
            """;

    MockPart data = new MockPart("data", invalidJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(multipart("/clubs/{clubId}/publications", clubId).part(data))
        .andExpect(status().isBadRequest());
  }
}
