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
class GetClubUserTest extends AbstractWebIntegrationTest {

  @Test
  void getClubUser_happyPath_returns200() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String createJson =
        """
        {
          "nif": "12345678A",
          "name": "Luis",
          "surname": "Delgado",
          "email": "luis@test.com",
          "birthDate": "1990-01-10",
          "address": "Calle Mayor 1",
          "city": "Madrid",
          "state": "Madrid",
          "postalCode": "28001",
          "phone": "600123123",
          "homePhone": "910000000"
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
        .perform(get("/clubs/{club}/users/{email}", club.slug(), "luis@test.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clubId").value(club.id()))
        .andExpect(jsonPath("$.nif").value("12345678A"))
        .andExpect(jsonPath("$.name").value("Luis"))
        .andExpect(jsonPath("$.surname").value("Delgado"))
        .andExpect(jsonPath("$.email").value("luis@test.com"))
        .andExpect(jsonPath("$.birthDate").value("1990-01-10"))
        .andExpect(jsonPath("$.address").value("Calle Mayor 1"))
        .andExpect(jsonPath("$.city").value("Madrid"))
        .andExpect(jsonPath("$.state").value("Madrid"))
        .andExpect(jsonPath("$.postalCode").value("28001"))
        .andExpect(jsonPath("$.phone").value("600123123"))
        .andExpect(jsonPath("$.homePhone").value("910000000"));
  }

  @Test
  void getClubUser_whenUserDoesNotExist_returns404() throws Exception {
    ClubInserted club = utilTest.insertClub();

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(get("/clubs/{club}/users/{email}", club.slug(), "missing@test.com"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getClubUser_withoutAuthentication_returns4xx() throws Exception {
    ClubInserted club = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{club}/users/{email}", club.slug(), "luis@test.com"))
        .andExpect(status().is4xxClientError());
  }
}
