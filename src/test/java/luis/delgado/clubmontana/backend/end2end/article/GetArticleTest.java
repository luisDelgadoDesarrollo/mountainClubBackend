package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetArticleTest extends AbstractWebIntegrationTest {

  @Test
  void getArticle_happyPath_returnsArticleWithImages() throws Exception {

    Long clubId = utilTest.insertClub();
    Long articleId = utilTest.createArticle(clubId);

    mockMvc
        .perform(get("/clubs/{clubId}/articles/{articleId}", clubId, articleId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagePath").isArray())
        .andExpect(jsonPath("$.imagePath.length()").value(2));
  }

  @Test
  void getArticle_whenArticleDoesNotExist_returns404() throws Exception {
    Long clubId = utilTest.insertClub();
    Long articleId = utilTest.createArticle(clubId);
    mockMvc
        .perform(get("/clubs/{clubId}/articles/{articleId}", clubId, articleId + 1))
        .andExpect(status().isNotFound());
  }

  @Test
  void getArticle_whenClubDoesNotMatch_returns404() throws Exception {

    Long clubId = utilTest.insertClub();
    Long articleId = utilTest.createArticle(clubId);

    mockMvc
        .perform(get("/clubs/{clubId}/articles/{articleId}", clubId + 1, articleId))
        .andExpect(status().isNotFound());
  }
}
