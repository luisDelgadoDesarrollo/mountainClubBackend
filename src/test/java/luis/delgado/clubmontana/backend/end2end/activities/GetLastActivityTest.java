package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.nio.file.Path;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GetLastActivityTest extends AbstractWebIntegrationTest {

  @TempDir static Path tempDir;
  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityRepository activityRepository;

  @Test
  void getLastActivity_happyPath_returnsArticleWithImages() throws Exception {

    ClubInserted club = utilTest.insertClub();
    utilTest.createActivity(club);

    mockMvc
        .perform(get("/clubs/{club}/activities/last", club.slug()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagesPath").isArray())
        .andExpect(jsonPath("$.imagesPath.length()").value(1));
  }

  @Test
  void getLastActivity_whenArticleDoesNotExist_returns404() throws Exception {
    ClubInserted club = utilTest.insertClub();
    mockMvc
        .perform(
            get("/clubs/{club}/activities/last", club.slug())
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().isNoContent());
  }
}
