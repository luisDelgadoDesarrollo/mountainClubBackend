package luis.delgado.clubmontana.backend.end2end.publications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
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

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> publication = utilTest.createPublication(club);
    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            delete(
                "/clubs/{club}/publications/{publication}", club.slug(), publication.getSecond()))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublication_notExistingPublication_returns204() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> publication = utilTest.createPublication(club);
    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            delete(
                "/clubs/{club}/publications/{publication}",
                club.slug(),
                publication.getSecond() + 1))
        .andExpect(status().isNotFound());
  }

  @Test
  void deletePublication_withoutAuthentication_returnsForbidden() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> publication = utilTest.createPublication(club);

    mockMvc
        .perform(
            delete("/clubs/{club}/publications/{publication}", club.slug(), publication.getSecond())
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().isForbidden());
  }
}
