package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"storage.images.base-path=${java.io.tmpdir}/images-test"})
@Transactional
public class GetActivityTest {

  @TempDir static Path tempDir;
  @Autowired EntityManager entityManager;
  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityRepository activityRepository;

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
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
  void getPublication_happyPath_returnsPublicationWithImages() throws Exception {

    Long clubId = insertClub();

    Activity a1 = new Activity();
    a1.setClubId(clubId);
    a1.setTitle("Pub 1");
    a1.setDescription("Texto 1");
    a1.setImages(List.of());
    a1.setAffiliatePrice(BigDecimal.ONE);
    a1.setNoAffiliatePrice(BigDecimal.ONE);
    a1.setStartDate(LocalDateTime.now());
    a1.setEndDate(LocalDateTime.now());

    Activity saved = activityRepository.saveActivity(a1);

    // 👇 MISMA RUTA QUE USA EL SERVICIO
    Path imagesDir =
        tempDir
            .resolve("club_" + clubId)
            .resolve(ImageType.ACTIVITY.name())
            .resolve("activity_" + saved.getActivityId());

    Files.createDirectories(imagesDir);
    Files.createFile(imagesDir.resolve("1.jpg"));
    Files.createFile(imagesDir.resolve("2.jpg"));

    mockMvc
        .perform(get("/clubs/{clubId}/activities/{activityId}", clubId, saved.getActivityId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagesPath").isArray())
        .andExpect(jsonPath("$.imagesPath.length()").value(2));
  }

  @Test
  void getPublication_whenPublicationDoesNotExist_returns404() throws Exception {

    mockMvc
        .perform(get("/clubs/{clubId}/activities/{activityId}", 1L, 999L))
        .andExpect(status().isNotFound());
  }

  @Test
  void getPublication_whenClubDoesNotMatch_returns404() throws Exception {

    Long clubId = insertClub();
    Activity a2 = new Activity();
    a2.setClubId(clubId);
    a2.setTitle("Pub 1");
    a2.setDescription("Texto 1");
    a2.setImages(List.of());
    a2.setAffiliatePrice(BigDecimal.ONE);
    a2.setNoAffiliatePrice(BigDecimal.ONE);
    a2.setStartDate(LocalDateTime.now());
    a2.setEndDate(LocalDateTime.now());

    Activity saved = activityRepository.saveActivity(a2);

    mockMvc
        .perform(get("/clubs/{clubId}/activities/{activityId}", clubId + 1, saved.getActivityId()))
        .andExpect(status().isNotFound());
  }
}
