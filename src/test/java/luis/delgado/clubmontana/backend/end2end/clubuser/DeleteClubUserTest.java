package luis.delgado.clubmontana.backend.end2end.clubuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubUserEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeleteClubUserTest extends AbstractWebIntegrationTest {

  @Autowired private ClubUserEntityJpa clubUserEntityJpa;

  @Test
  void deleteClubUser_happyPath_returns204_andDeletes() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String createJson =
        """
        {
          "nif": "12345678A",
          "name": "Luis",
          "email": "luis@test.com"
        }
        """;

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
        .andExpect(status().isCreated());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(delete("/clubs/{club}/users/{email}", club.slug(), "luis@test.com"))
        .andExpect(status().isNoContent());

    assertThat(clubUserEntityJpa.findByClubIdAndEmail(club.id(), "luis@test.com")).isEmpty();
  }

  @Test
  void deleteClubUser_notExistingUser_returns204() throws Exception {
    ClubInserted club = utilTest.insertClub();
    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(delete("/clubs/{club}/users/{email}", club.slug(), "missing@test.com"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteClubUser_withoutAuthentication_returns4xx() throws Exception {
    ClubInserted club = utilTest.insertClub();

    mockMvc
        .perform(delete("/clubs/{club}/users/{email}", club.slug(), "luis@test.com"))
        .andExpect(status().is4xxClientError());
  }
}
