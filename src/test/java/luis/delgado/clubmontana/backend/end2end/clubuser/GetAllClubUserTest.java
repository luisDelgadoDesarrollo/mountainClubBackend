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
          "surname": "Prueba",
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
    String user2 =
        """
        {
          "nif": "87654321B",
          "name": "Ana",
          "surname": "Prueba",
          "email": "ana@test.com",
          "birthDate": "1992-03-15",
          "address": "Avenida Central 4",
          "city": "Sevilla",
          "state": "Andalucia",
          "postalCode": "41001",
          "phone": "611111111",
          "homePhone": "955555555"
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
