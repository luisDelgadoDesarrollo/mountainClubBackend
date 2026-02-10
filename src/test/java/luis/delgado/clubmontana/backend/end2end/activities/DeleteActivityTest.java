package luis.delgado.clubmontana.backend.end2end.activities;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteActivityTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ActivityEntityJpa activityEntityJpa;

  @Test
  void deletePublication_existingPublication_returns204() throws Exception {
    Long clubId = utilTest.insertClub();
    Long activityId = utilTest.createActivity(clubId);
    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(delete("/clubs/{clubId}/activities/{activityId}", clubId, activityId))
        .andExpect(status().isNoContent());

    Optional<ActivityEntity> activityOptional = activityEntityJpa.findById(activityId);
    assertThat(activityOptional).isNotPresent();
  }

  @Test
  void deletePublication_notExistingPublication_returns204() throws Exception {
    Long clubId = utilTest.insertClub();
    long activityId = utilTest.createActivity(clubId);
    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(delete("/clubs/{clubId}/activities/{activityId}", clubId, activityId + 1))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublication_withoutAuthentication_returnsForbidden() throws Exception {
    mockMvc
        .perform(delete("/clubs/{clubId}/activities/{activityId}", 1L, 1L))
        .andExpect(status().isForbidden());
  }
}
