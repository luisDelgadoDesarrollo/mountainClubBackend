package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
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
@Transactional
class GetPublicationsTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getPublications_happyPath_returnsPublicationsWithImages() throws Exception {

    Long clubId = utilTest.insertClub();
    Long publication1 = utilTest.createPublication(clubId);
    Long publication2 = utilTest.createPublication(clubId);

    // filesystem
    utilTest.createImage(publication1, clubId, "1.jpg");
    utilTest.createImage(publication2, clubId, "2.jpg");

    mockMvc
        .perform(
            get("/clubs/{clubId}/publications", clubId)
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

    Long clubId = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{clubId}/publications", clubId).param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }
}
