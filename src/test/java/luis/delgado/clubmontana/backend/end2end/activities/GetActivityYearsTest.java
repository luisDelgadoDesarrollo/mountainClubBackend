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
class GetActivityYearsTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityEntityJpa activityEntityJpa;

  @Test
  void getYears_happyPath_returnsDistinctYearsOrderedDesc() throws Exception {
    ClubInserted club = utilTest.insertClub();

    Pair<Long, String> activity2026 = utilTest.createActivity(club);
    Pair<Long, String> activity2025 = utilTest.createActivity(club);
    Pair<Long, String> another2026 = utilTest.createActivity(club);

    updateActivityDates(activity2026.getFirst(), LocalDateTime.of(2026, 3, 15, 8, 30));
    updateActivityDates(activity2025.getFirst(), LocalDateTime.of(2025, 5, 10, 9, 0));
    updateActivityDates(another2026.getFirst(), LocalDateTime.of(2026, 9, 20, 7, 45));

    mockMvc
        .perform(get("/clubs/{club}/activities/years", club.slug()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0]").value(2026))
        .andExpect(jsonPath("$[1]").value(2025));
  }

  @Test
  void getYears_whenNoActivitiesExist_returnsEmptyList() throws Exception {
    ClubInserted club = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{club}/activities/years", club.slug()))
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
