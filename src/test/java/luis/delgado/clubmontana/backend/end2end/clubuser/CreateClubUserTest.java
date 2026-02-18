package luis.delgado.clubmontana.backend.end2end.clubuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubUserEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ClubUserIdEntity;
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
class CreateClubUserTest extends AbstractWebIntegrationTest {

  @Autowired private ClubUserEntityJpa clubUserEntityJpa;

  @Test
  void createClubUser_happyPath_returns201_andPersists() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String json =
        """
        {
          "nif": "12345678A",
          "name": "Luis Delgado",
          "email": "luis@test.com"
        }
        """;

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated());

    ClubUserIdEntity id = new ClubUserIdEntity(club.id(), "luis@test.com");
    ClubUserEntity saved = clubUserEntityJpa.findById(id).orElse(null);

    assertThat(saved).isNotNull();
    assertThat(saved.getClubId()).isEqualTo(club.id());
    assertThat(saved.getNif()).isEqualTo("12345678A");
    assertThat(saved.getName()).isEqualTo("Luis Delgado");
    assertThat(saved.getEmail()).isEqualTo("luis@test.com");
  }

  @Test
  void createClubUser_withoutAuthentication_returns4xx() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String json =
        """
        {
          "nif": "12345678A",
          "name": "Luis Delgado",
          "email": "luis@test.com"
        }
        """;

    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createClubUser_invalidJson_returns400() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String invalidJson =
        """
        {
          "nif": "12345678A",
          "name": "Luis Delgado",
          "email": "luis@test.com",
        }
        """;

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createClubUser_whenAlreadyExists_fails() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String json =
        """
        {
          "nif": "12345678A",
          "name": "Luis Delgado",
          "email": "luis@test.com"
        }
        """;

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/users", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isConflict());
  }
}
