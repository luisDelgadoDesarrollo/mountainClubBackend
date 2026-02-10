package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteArticleTest extends AbstractWebIntegrationTest {

  @Test
  void deleteArticle_happyPath() throws Exception {
    Long clubId = utilTest.insertClub();
    Long articleId = utilTest.createArticle(clubId);
    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(delete("/clubs/{clubId}/articles/{articleId}", clubId, articleId))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteArticle_withoutAuthentication_returnsForbidden() throws Exception {
    Long clubId = utilTest.insertClub();
    Long articleId = utilTest.createArticle(clubId);

    mockMvc
        .perform(
            delete("/clubs/{clubId}/articles/{articleId}", clubId, articleId)
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().isForbidden());
  }

  @Test
  void deleteArticle_activityDoesNotExists() throws Exception {
    Long clubId = utilTest.insertClub();
    Long articleId = utilTest.createArticle(clubId);
    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(delete("/clubs/{clubId}/articles/{articleId}", clubId, articleId + 1))
        .andExpect(status().isNotFound());
  }
}
