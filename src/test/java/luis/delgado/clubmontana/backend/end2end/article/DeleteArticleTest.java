package luis.delgado.clubmontana.backend.end2end.article;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteArticleTest {

  @Autowired private MockMvc mockMvc;
//todo test tengo que crear los datos antes de comprobar

  @Test
  void deletePublication_withoutAuthentication_returnsForbidden() throws Exception {

    mockMvc
        .perform(delete("/clubs/{clubId}/articles/{articleId}", 1L, 1L))
        .andExpect(status().isForbidden());
  }
}
