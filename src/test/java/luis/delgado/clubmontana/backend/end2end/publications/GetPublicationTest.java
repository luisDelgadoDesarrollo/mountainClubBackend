package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GetPublicationTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getPublication_happyPath_returnsPublicationWithImages() throws Exception {

    Long clubId = utilTest.insertClub();
    Long publicationId = utilTest.createPublication(clubId);


    mockMvc
        .perform(get("/clubs/{clubId}/publications/{publicationId}", clubId, publicationId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagesPath").isArray())
        .andExpect(jsonPath("$.imagesPath.length()").value(1));
  }

  @Test
  void getPublication_whenPublicationDoesNotExist_returns404() throws Exception {

    mockMvc
        .perform(get("/clubs/{clubId}/publications/{publicationId}", 1L, 999L))
        .andExpect(status().isNotFound());
  }

  @Test
  void getPublication_whenClubDoesNotMatch_returns404() throws Exception {

    Long clubId = utilTest.insertClub();
    Long publicationId = utilTest.createPublication(clubId);

    mockMvc
        .perform(get("/clubs/{clubId}/publications/{publicationId}", clubId + 1, publicationId))
        .andExpect(status().isNotFound());
  }
}
