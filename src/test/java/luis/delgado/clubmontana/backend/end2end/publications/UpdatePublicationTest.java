package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UpdatePublicationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private PublicationRepository publicationRepository;
  @Autowired private UtilTest utilTest;

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
  void updatePublication_happyPath_returns201() throws Exception {

    Long clubId = utilTest.insertClub();
    Publication publication = new Publication();
    publication.setClubId(clubId);
    publication.setTitle("Original title");
    publication.setText("Original text");
    publication.setImages(List.of());
    publication.setLinks(List.of());

    Publication savedPublication = publicationRepository.savePublication(publication);
    Long publicationId = savedPublication.getPublicationId();

    String json =
        """
            {
              "title": "Updated title",
              "text": "Updated text",
              "images": [
                { "image": "image-1", "description": "desc" }
              ],
              "links": []
            }
            """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "image-1",
            "photo.jpg",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(clubId);

    // when / then
    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{clubId}/publications/{publicationId}",
                    clubId,
                    publicationId)
                .part(data)
                .file(image))
        .andExpect(status().isCreated());
  }
}
