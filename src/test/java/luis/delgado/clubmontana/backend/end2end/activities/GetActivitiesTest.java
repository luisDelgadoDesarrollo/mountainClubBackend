package luis.delgado.clubmontana.backend.end2end.activities;

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
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GetActivitiesTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getActivity_happyPath_returnsActivityWithImages() throws Exception {

    ClubInserted club = utilTest.insertClub();

    Pair<Long, String> activity = utilTest.createActivity(club);
    Pair<Long, String> activity2 = utilTest.createActivity(club);

    utilTest.createImage(activity.getFirst(), club.id(), "1.jpg");
    utilTest.createImage(activity2.getFirst(), club.id(), "2.jpg");

    mockMvc
        .perform(
            get("/clubs/{club}/activities", club.slug())
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].imagesPath").isArray())
        .andExpect(jsonPath("$[0].imagesPath.length()").value(1))
        .andExpect(jsonPath("$[1].imagesPath").isArray())
        .andExpect(jsonPath("$[1].imagesPath.length()").value(1));
  }

  @Test
  void getPublications_whenNoPublications_returnsEmptyList() throws Exception {

    ClubInserted club = utilTest.insertClub();

    mockMvc
        .perform(
            get("/clubs/{club}/activities", club.slug()).param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }
}
