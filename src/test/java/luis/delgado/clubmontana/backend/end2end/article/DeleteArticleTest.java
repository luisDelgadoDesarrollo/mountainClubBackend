package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteArticleTest extends AbstractWebIntegrationTest {

  @Test
  void deleteArticle_happyPath() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);
    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(delete("/clubs/{club}/articles/{article}", club.slug(), article.getSecond()))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteArticle_withoutAuthentication_returnsForbidden() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);

    mockMvc
        .perform(
            delete("/clubs/{club}/articles/{article}", club.id(), article.getSecond())
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().isForbidden());
  }

  @Test
  void deleteArticle_activityDoesNotExists() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);
    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(delete("/clubs/{club}/articles/{article}", club.slug(), article.getSecond() + 1))
        .andExpect(status().isNotFound());
  }
}
