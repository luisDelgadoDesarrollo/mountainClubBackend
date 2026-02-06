package luis.delgado.clubmontana.backend.end2end.article;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
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
class CreateArticleTest {

  @Autowired EntityManager entityManager;
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

  private Long insertClub() {
    ClubEntity club =
        ClubEntity.builder()
            .name("Club test")
            .nif("NIF-" + System.nanoTime())
            .url("club-" + System.nanoTime() + ".es")
            .hasInicio(true)
            .hasSecciones(true)
            .hasGaleria(true)
            .hasEnlaces(true)
            .hasContacto(true)
            .hasFederarse(true)
            .hasTienda(true)
            .hasCalendario(true)
            .hasConocenos(true)
            .hasNoticias(true)
            .hasForo(true)
            .hasEstatutos(true)
            .hasNormas(true)
            .hasHazteSocio(true)
            .build();

    entityManager.persist(club);
    entityManager.flush();
    return club.getClubId();
  }

  @Test
  void shouldCreateArticleWithImagesAndVariants() throws Exception {
    Long clubId = insertClub();
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

    UtilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(multipart("/clubs/{clubId}/articles", clubId).part(data).file(image1).file(image2))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
  }
}
