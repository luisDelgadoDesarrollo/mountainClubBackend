package luis.delgado.clubmontana.backend.end2end.clubuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class UpdateClubUserTest extends AbstractWebIntegrationTest {

  @Autowired private ClubUserEntityJpa clubUserEntityJpa;

  @Test
  void updateClubUser_happyPath_returns202_andUpdatesFields() throws Exception {
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
    String updateJson =
        """
        {
          "nif": "12345678A",
          "name": "Luis Actualizado",
          "surname": "Delgado Actualizado",
          "email": "otro-email@test.com",
          "birthDate": "1991-02-11",
          "address": "Calle Nueva 2",
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
                .content(createJson))
        .andExpect(status().isCreated());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            put("/clubs/{club}/users/{email}", club.slug(), "luis@test.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
        .andExpect(status().isAccepted());

    ClubUserEntity saved =
        clubUserEntityJpa.findById(new ClubUserIdEntity(club.id(), "luis@test.com")).orElse(null);

    assertThat(saved).isNotNull();
    assertThat(saved.getEmail()).isEqualTo("luis@test.com");
    assertThat(saved.getName()).isEqualTo("Luis Actualizado");
    assertThat(saved.getSurname()).isEqualTo("Delgado Actualizado");
    assertThat(saved.getBirthDate()).hasToString("1991-02-11");
    assertThat(saved.getAddress()).isEqualTo("Calle Nueva 2");
    assertThat(saved.getCity()).isEqualTo("Sevilla");
    assertThat(saved.getState()).isEqualTo("Andalucia");
    assertThat(saved.getPostalCode()).isEqualTo("41001");
    assertThat(saved.getPhone()).isEqualTo("611111111");
    assertThat(saved.getHomePhone()).isEqualTo("955555555");
  }

  @Test
  void updateClubUser_withoutAuthentication_returns4xx() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String json =
        """
        {
          "nif": "12345678A",
          "name": "Luis",
          "email": "luis@test.com"
        }
        """;

    mockMvc
        .perform(
            put("/clubs/{club}/users/{email}", club.slug(), "luis@test.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void updateClubUser_invalidJson_returns400() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String createJson =
        """
        {
          "nif": "12345678A",
          "name": "Luis",
          "email": "luis@test.com"
        }
        """;
    String invalidJson =
        """
        {
          "nif": "12345678A",
          "name": "Luis",
          "email": "luis@test.com",
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
        .perform(
            put("/clubs/{club}/users/{email}", club.slug(), "luis@test.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());
  }
}
