package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
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
public class GetActivitiesTest {

  @TempDir static Path tempDir;
  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityRepository activityRepository;
  @Autowired private EntityManager entityManager;

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("storage.images.base-path", () -> tempDir.toString());
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
  void getActivity_happyPath_returnsActivityWithImages() throws Exception {

    Long clubId = insertClub();

    // publicación 1
    Activity a1 = new Activity();
    a1.setClubId(clubId);
    a1.setTitle("Pub 1");
    a1.setDescription("Texto 1");
    a1.setImages(List.of());
    a1.setAffiliatePrice(BigDecimal.ONE);
    a1.setNoAffiliatePrice(BigDecimal.ONE);
    a1.setStartDate(LocalDateTime.now());
    a1.setEndDate(LocalDateTime.now());

    Activity saved1 = activityRepository.saveActivity(a1);

    // publicación 2
    Activity a2 = new Activity();
    a2.setClubId(clubId);
    a2.setTitle("Pub 1");
    a2.setDescription("Texto 1");
    a2.setImages(List.of());
    a2.setAffiliatePrice(BigDecimal.ONE);
    a2.setNoAffiliatePrice(BigDecimal.ONE);
    a2.setStartDate(LocalDateTime.now());
    a2.setEndDate(LocalDateTime.now());

    Activity saved2 = activityRepository.saveActivity(a2);

    // filesystem
    createImage(saved1.getActivityId(), clubId, "1.jpg");
    createImage(saved2.getActivityId(), clubId, "2.jpg");

    mockMvc
        .perform(
            get("/clubs/{clubId}/activities", clubId)
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

    Long clubId = insertClub();

    mockMvc
        .perform(get("/clubs/{clubId}/activities", clubId).param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  // 🔧 helper
  private void createImage(Long publicationId, Long clubId, String filename) throws IOException {
    Path dir =
        tempDir
            .resolve("club_" + clubId)
            .resolve(ImageType.ACTIVITY.name())
            .resolve("activity_" + publicationId);

    Files.createDirectories(dir);
    Files.createFile(dir.resolve(filename));
  }
}
