package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.AfterAll;
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
public class UpdateArticleTest {

  @Autowired private UtilTest utilTest;
  @Autowired private MockMvc mockMvc;

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


  @Test
  void shouldUpdateArticleWithImagesAndVariants() throws Exception {
    Long clubId = utilTest.insertClub();

    // ---------- CREATE ----------
    String createJson =
        """
                    {
                      "title": "Artículo original",
                      "description": "Descripción original",
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

    MockPart createPart = new MockPart("article", createJson.getBytes(StandardCharsets.UTF_8));
    createPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile createImage1 =
        new MockMultipartFile(
            "image-1",
            "image-1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    MockMultipartFile createImage2 =
        new MockMultipartFile(
            "image-2",
            "image-2.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(clubId);

    String response =
        mockMvc
            .perform(
                multipart("/clubs/{clubId}/articles", clubId)
                    .part(createPart)
                    .file(createImage1)
                    .file(createImage2))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long articleId =
        Long.valueOf(
            response.replaceAll("\\D+", "") // simple y suficiente para test
            );

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
            multipart("/clubs/{clubId}/articles/{articleId}", clubId, articleId)
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
        .andExpect(jsonPath("$.id").value(articleId));
  }
}
