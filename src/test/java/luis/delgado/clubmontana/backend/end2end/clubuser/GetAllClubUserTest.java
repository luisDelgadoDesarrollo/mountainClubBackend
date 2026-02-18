package luis.delgado.clubmontana.backend.end2end.clubuser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GetAllClubUserTest extends AbstractWebIntegrationTest {

  @Test
  void getAllClubUsers_happyPath_returnsPageWithUsers() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String user1 =
        """
        {
          "nif": "12345678A",
          "name": "Luis",
          "email": "luis@test.com"
        }
        """;
    String user2 =
        """
        {
          "nif": "87654321B",
          "name": "Ana",
          "email": "ana@test.com"
        }
        """;

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(user1))
        .andExpect(status().isCreated());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(user2))
        .andExpect(status().isCreated());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            get("/clubs/{club}/users", club.slug())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "nif,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.totalElements").value(2));
  }

  @Test
  void getAllClubUsers_whenNoUsers_returnsEmptyPage() throws Exception {
    ClubInserted club = utilTest.insertClub();
    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(
            get("/clubs/{club}/users", club.slug())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "nif,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(0))
        .andExpect(jsonPath("$.totalElements").value(0));
  }
}
