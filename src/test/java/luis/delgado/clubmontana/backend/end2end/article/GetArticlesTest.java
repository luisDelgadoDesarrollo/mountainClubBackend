package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetArticlesTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getArticles_happyPath_returnsArticleWithImages() throws Exception {

    ClubInserted club = utilTest.insertClub();

    Pair<Long, String> article = utilTest.createArticle(club);
    Pair<Long, String> article2 = utilTest.createArticle(club);

    utilTest.createImage(article.getFirst(), club.id(), "1.jpg");
    utilTest.createImage(article2.getFirst(), club.id(), "2.jpg");

    mockMvc
        .perform(
            get("/clubs/{club}/articles", club.slug())
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].imagePath").isArray())
        .andExpect(jsonPath("$.content[0].imagePath.length()").value(1))
        .andExpect(jsonPath("$.content[1].imagePath").isArray())
        .andExpect(jsonPath("$.content[1].imagePath.length()").value(1));
  }

  @Test
  void getPublications_whenNoPublications_returnsEmptyList() throws Exception {

    ClubInserted club = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{club}/articles", club.slug()).param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.content.length()").value(0));
  }
}
