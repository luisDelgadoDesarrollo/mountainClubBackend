package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GetPublicationsTest {

  @TempDir static Path tempDir;
  @Autowired private MockMvc mockMvc;
  @Autowired private PublicationRepository publicationRepository;
  @Autowired private UtilTest utilTest;

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("storage.images.base-path", () -> tempDir.toString());
  }

  @Test
  void getPublications_happyPath_returnsPublicationsWithImages() throws Exception {

    Long clubId = utilTest.insertClub();

    // publicación 1
    Publication p1 = new Publication();
    p1.setClubId(clubId);
    p1.setTitle("Pub 1");
    p1.setText("Texto 1");
    p1.setImages(List.of());
    p1.setLinks(List.of());

    Publication saved1 = publicationRepository.savePublication(p1);

    // publicación 2
    Publication p2 = new Publication();
    p2.setClubId(clubId);
    p2.setTitle("Pub 2");
    p2.setText("Texto 2");
    p2.setImages(List.of());
    p2.setLinks(List.of());

    Publication saved2 = publicationRepository.savePublication(p2);

    // filesystem
    createImage(saved1.getPublicationId(), clubId, "1.jpg");
    createImage(saved2.getPublicationId(), clubId, "2.jpg");

    mockMvc
        .perform(
            get("/clubs/{clubId}/publications", clubId)
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].imagesPath").isArray())
        .andExpect(jsonPath("$[0].imagesPath.length()").value(1));
  }

  @Test
  void getPublications_whenNoPublications_returnsEmptyList() throws Exception {

    Long clubId = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{clubId}/publications", clubId).param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  // 🔧 helper
  private void createImage(Long publicationId, Long clubId, String filename) throws IOException {
    Path dir =
        tempDir
            .resolve("club_" + clubId)
            .resolve(ImageType.PUBLICATION.name())
            .resolve("publication_" + publicationId);

    Files.createDirectories(dir);
    Files.createFile(dir.resolve(filename));
  }
}
