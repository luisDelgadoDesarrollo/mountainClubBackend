package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
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
public class GetLastPublicationTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getPublication_happyPath_returnsPublicationWithImages() throws Exception {

    ClubInserted club = utilTest.insertClub();
    utilTest.createPublication(club);

    mockMvc
        .perform(get("/clubs/{club}/publications/last", club.slug()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.imagesPath").isArray())
        .andExpect(jsonPath("$.imagesPath.length()").value(1));
  }

  @Test
  void getPublication_whenPublicationDoesNotExist_returns404() throws Exception {
    ClubInserted club = utilTest.insertClub();
    mockMvc
        .perform(get("/clubs/{club}/publications/last", club.id()))
        .andExpect(status().isNotFound());
  }
}
