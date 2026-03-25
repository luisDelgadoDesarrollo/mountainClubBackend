package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GetActivitiesByYearTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityEntityJpa activityEntityJpa;

  @Test
  void getActivitiesByYear_happyPath_returnsOnlyActivitiesFromYear() throws Exception {
    ClubInserted club = utilTest.insertClub();

    Pair<Long, String> activity2026 = utilTest.createActivity(club);
    Pair<Long, String> activity2025 = utilTest.createActivity(club);

    updateActivityDates(activity2026.getFirst(), LocalDateTime.of(2026, 3, 15, 8, 30));
    updateActivityDates(activity2025.getFirst(), LocalDateTime.of(2025, 5, 10, 9, 0));

    utilTest.createImage(activity2026.getFirst(), club.id(), "2026.jpg");
    utilTest.createImage(activity2025.getFirst(), club.id(), "2025.jpg");

    mockMvc
        .perform(get("/clubs/{club}/activities/years/{year}", club.slug(), 2026))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].slug").value(activity2026.getSecond()))
        .andExpect(jsonPath("$[0].startDate").value("2026-03-15T08:30:00"))
        .andExpect(jsonPath("$[0].imagesPath").isArray())
        .andExpect(jsonPath("$[0].imagesPath.length()").value(1));
  }

  @Test
  void getActivitiesByYear_whenNoActivitiesMatch_returnsEmptyList() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    updateActivityDates(activity.getFirst(), LocalDateTime.of(2026, 3, 15, 8, 30));

    mockMvc
        .perform(get("/clubs/{club}/activities/years/{year}", club.slug(), 2024))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  private void updateActivityDates(Long activityId, LocalDateTime startDate) {
    var activity = activityEntityJpa.findById(activityId).orElseThrow();
    activity.setStartDate(startDate);
    activity.setEndDate(startDate.plusHours(8));
    activityEntityJpa.save(activity);
  }
}
