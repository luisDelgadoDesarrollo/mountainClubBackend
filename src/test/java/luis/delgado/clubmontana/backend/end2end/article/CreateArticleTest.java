package luis.delgado.clubmontana.backend.end2end.article;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CreateArticleTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldCreateArticleWithImagesAndVariants() throws Exception {
    Long clubId = utilTest.insertClub();
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

    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(multipart("/clubs/{clubId}/articles", clubId).part(data).file(image1).file(image2))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  void createArticle_withoutAuthentication() throws Exception {
    Long clubId = utilTest.insertClub();
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

    mockMvc
        .perform(multipart("/clubs/{clubId}/articles", clubId).part(data).file(image1).file(image2))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createArticle_badPayload() throws Exception {
    Long clubId = utilTest.insertClub();
    String articleJson =
        """
                    {
                      "text": "json incorrecto"
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

    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(multipart("/clubs/{clubId}/articles", clubId).part(data).file(image1).file(image2))
        .andExpect(status().is4xxClientError());
  }
}
