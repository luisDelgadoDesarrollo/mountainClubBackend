package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.nio.file.Path;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GetActivityTest extends AbstractWebIntegrationTest {

  @TempDir static Path tempDir;
  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityRepository activityRepository;

  @Test
  void getActivities_happyPath_returnsArticleWithImages() throws Exception {

    Long clubId = utilTest.insertClub();
    Long activityId = utilTest.createActivity(clubId);

    Path imagesDir =
        tempDir
            .resolve("club_" + clubId)
            .resolve(ImageType.ACTIVITY.name())
            .resolve("activity_" + activityId);

    mockMvc
        .perform(get("/clubs/{clubId}/activities/{activityId}", clubId, activityId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagesPath").isArray())
        .andExpect(jsonPath("$.imagesPath.length()").value(1));
  }

  @Test
  void getArticle_whenArticleDoesNotExist_returns404() throws Exception {
    Long clubId = utilTest.insertClub();
    Long activityId = utilTest.createActivity(clubId);
    mockMvc
        .perform(get("/clubs/{clubId}/activities/{activityId}", clubId, activityId + 1))
        .andExpect(status().isNotFound());
  }

  @Test
  void getArticle_whenClubDoesNotMatch_returns404() throws Exception {

    Long clubId = utilTest.insertClub();
    Long activityId = utilTest.createActivity(clubId);

    mockMvc
        .perform(get("/clubs/{clubId}/activities/{activityId}", clubId + 1, activityId))
        .andExpect(status().isNotFound());
  }
}
