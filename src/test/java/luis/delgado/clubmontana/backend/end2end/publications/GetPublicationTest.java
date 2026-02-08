package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GetPublicationTest {

  @TempDir static Path tempDir;
  @Autowired private MockMvc mockMvc;
  @Autowired private PublicationRepository publicationRepository;
  @Autowired private UtilTest utilTest;

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("storage.images.base-path", () -> tempDir.toString());
  }

  @Test
  void getPublication_happyPath_returnsPublicationWithImages() throws Exception {

    Long clubId = utilTest.insertClub();

    Publication publication = new Publication();
    publication.setClubId(clubId);
    publication.setTitle("Ruta al Mulhacén");
    publication.setText("Texto");
    publication.setImages(List.of(new Image(null, null, "img-1", "desc")));
    publication.setLinks(List.of());

    Publication saved = publicationRepository.savePublication(publication);

    // 👇 MISMA RUTA QUE USA EL SERVICIO
    Path imagesDir =
        tempDir
            .resolve("club_" + clubId)
            .resolve(ImageType.PUBLICATION.name())
            .resolve("publication_" + saved.getPublicationId());

    Files.createDirectories(imagesDir);
    Files.createFile(imagesDir.resolve("1.jpg"));
    Files.createFile(imagesDir.resolve("2.jpg"));

    mockMvc
        .perform(
            get("/clubs/{clubId}/publications/{publicationId}", clubId, saved.getPublicationId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagesPath").isArray())
        .andExpect(jsonPath("$.imagesPath.length()").value(2));
  }

  @Test
  void getPublication_whenPublicationDoesNotExist_returns404() throws Exception {

    mockMvc
        .perform(get("/clubs/{clubId}/publications/{publicationId}", 1L, 999L))
        .andExpect(status().isNotFound());
  }

  @Test
  void getPublication_whenClubDoesNotMatch_returns404() throws Exception {

    Publication publication = new Publication();
    Long clubId = utilTest.insertClub();
    publication.setClubId(clubId);
    publication.setTitle("Titulo");
    publication.setText("Texto");
    publication.setImages(List.of());
    publication.setLinks(List.of());

    Publication saved = publicationRepository.savePublication(publication);

    mockMvc
        .perform(
            get(
                "/clubs/{clubId}/publications/{publicationId}",
                clubId + 1,
                saved.getPublicationId()))
        .andExpect(status().isNotFound());
  }
}
