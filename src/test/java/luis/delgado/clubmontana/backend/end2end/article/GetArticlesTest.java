package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetArticlesTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getArticle_happyPath_returnsArticleWithImages() throws Exception {

    Long clubId = utilTest.insertClub();

    Long article1 = utilTest.createArticle(clubId);
    Long article2 = utilTest.createArticle(clubId);

    utilTest.createImage(article1, clubId, "1.jpg");
    utilTest.createImage(article2, clubId, "2.jpg");

    mockMvc
        .perform(
            get("/clubs/{clubId}/articles", clubId)
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].imagePath").isArray())
        .andExpect(jsonPath("$[0].imagePath.length()").value(2))
        .andExpect(jsonPath("$[1].imagePath").isArray())
        .andExpect(jsonPath("$[1].imagePath.length()").value(2));
  }

  @Test
  void getPublications_whenNoPublications_returnsEmptyList() throws Exception {

    Long clubId = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{clubId}/articles", clubId).param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }
}
