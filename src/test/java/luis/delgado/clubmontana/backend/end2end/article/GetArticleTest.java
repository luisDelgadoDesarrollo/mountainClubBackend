package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetArticleTest extends AbstractWebIntegrationTest {

  @Test
  void getArticle_happyPath_returnsArticleWithImages() throws Exception {

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);

    mockMvc
        .perform(get("/clubs/{club}/articles/{article}", club.slug(), article.getSecond()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagePath").isArray())
        .andExpect(jsonPath("$.imagePath.length()").value(1));
  }

  @Test
  void getArticle_whenArticleDoesNotExist_returns404() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);
    mockMvc
        .perform(get("/clubs/{club}/articles/{article}", club.slug(), article.getSecond() + 1))
        .andExpect(status().isNotFound());
  }

  @Test
  void getArticle_whenClubDoesNotMatch_returns404() throws Exception {

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> article = utilTest.createArticle(club);

    mockMvc
        .perform(get("/clubs/{club}/articles/{article}", club.slug() + "1", article.getSecond()))
        .andExpect(status().isNotFound());
  }
}
