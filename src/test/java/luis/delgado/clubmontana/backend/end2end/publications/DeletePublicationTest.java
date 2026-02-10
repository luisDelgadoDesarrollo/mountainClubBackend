package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeletePublicationTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void deletePublication_existingPublication_returns204() throws Exception {

    Long clubId = utilTest.insertClub();
    Long publicationId = utilTest.createPublication(clubId);
    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(delete("/clubs/{clubId}/publications/{publicationId}", clubId, publicationId))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublication_notExistingPublication_returns204() throws Exception {
    Long clubId = utilTest.insertClub();
    Long publicationId = utilTest.createPublication(clubId);
    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(delete("/clubs/{clubId}/publications/{publicationId}", clubId, publicationId + 1))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublication_withoutAuthentication_returnsForbidden() throws Exception {
    Long clubId = utilTest.insertClub();
    Long publicationId = utilTest.createPublication(clubId);

    mockMvc
        .perform(
            delete("/clubs/{clubId}/publications/{publicationId}", clubId, publicationId)
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().isForbidden());
  }
}
