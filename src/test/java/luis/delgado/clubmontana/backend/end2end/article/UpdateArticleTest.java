package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UpdateArticleTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldUpdateArticleWithImagesAndVariants() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);
    // ---------- UPDATE ----------
    String updateJson =
        """
                    {
                      "title": "Artículo actualizado",
                      "description": "Descripción actualizada",
                      "images": [
                        { "image": "image-3" }
                      ],
                      "variants": [
                        {
                          "size": "L",
                          "color": "Azul",
                          "stock": 10,
                          "images": [
                            { "image": "image-4" }
                          ]
                        }
                      ]
                    }
                    """;

    MockPart updatePart = new MockPart("article", updateJson.getBytes(StandardCharsets.UTF_8));
    updatePart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile updateImage1 =
        new MockMultipartFile(
            "image-3",
            "image-3.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    MockMultipartFile updateImage2 =
        new MockMultipartFile(
            "image-4",
            "image-4.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockMvc
        .perform(
            multipart("/clubs/{club}/articles/{article}", club.slug(), article.getSecond())
                .part(updatePart)
                .file(updateImage1)
                .file(updateImage2)
                // IMPORTANTE: forzar PUT en multipart
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(article.getFirst()));
  }

  @Test
  void updateArticle_withoutAuthentication() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);
    // ---------- UPDATE ----------
    String updateJson =
        """
                        {
                          "title": "Artículo actualizado",
                          "description": "Descripción actualizada",
                          "images": [
                            { "image": "image-3" }
                          ],
                          "variants": [
                            {
                              "size": "L",
                              "color": "Azul",
                              "stock": 10,
                              "images": [
                                { "image": "image-4" }
                              ]
                            }
                          ]
                        }
                        """;

    MockPart updatePart = new MockPart("article", updateJson.getBytes(StandardCharsets.UTF_8));
    updatePart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile updateImage1 =
        new MockMultipartFile(
            "image-3",
            "image-3.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    MockMultipartFile updateImage2 =
        new MockMultipartFile(
            "image-4",
            "image-4.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/articles/{article}",
                    club.slug(),
                    article.getSecond())
                .part(updatePart)
                .file(updateImage1)
                .file(updateImage2)
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void updateArticle_badPayload() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);

    String updateJson =
        """
                        {
                          "text": "json invalido"
                        }
                        """;

    MockPart updatePart = new MockPart("article", updateJson.getBytes(StandardCharsets.UTF_8));
    updatePart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile updateImage1 =
        new MockMultipartFile(
            "image-3",
            "image-3.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    MockMultipartFile updateImage2 =
        new MockMultipartFile(
            "image-4",
            "image-4.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            multipart("/clubs/{club}/articles/{article}", club.slug(), article.getSecond())
                .part(updatePart)
                .file(updateImage1)
                .file(updateImage2))
        .andExpect(status().is4xxClientError());
  }
}
