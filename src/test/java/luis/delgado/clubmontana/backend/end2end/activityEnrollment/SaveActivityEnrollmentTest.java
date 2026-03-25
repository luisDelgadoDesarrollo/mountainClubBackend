package luis.delgado.clubmontana.backend.end2end.activityEnrollment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEnrollmentEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ActivityEnrollmentIdEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEnrollmentEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SaveActivityEnrollmentTest extends AbstractWebIntegrationTest {

  @Autowired private ActivityEnrollmentEntityJpa activityEnrollmentEntityJpa;

  @Test
  void saveActivityEnrollment_happyPath_returns201_andPersists() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    String json =
        """
        {
          "activityId": %d,
          "name": "Luis",
          "surname": "Delgado",
          "nif": "12345678Z",
          "email": "luis@test.com",
          "paid": false
        }
        """
            .formatted(activity.getFirst());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/activityEnrollment", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated());

    ActivityEnrollmentIdEntity id =
        new ActivityEnrollmentIdEntity(activity.getFirst(), "12345678Z");
    ActivityEnrollmentEntity saved = activityEnrollmentEntityJpa.findById(id).orElse(null);

    assertThat(saved).isNotNull();
    assertThat(saved.getActivityId()).isEqualTo(activity.getFirst());
    assertThat(saved.getName()).isEqualTo("Luis");
    assertThat(saved.getSurname()).isEqualTo("Delgado");
    assertThat(saved.getEmail()).isEqualTo("luis@test.com");
    assertThat(saved.getPaid()).isFalse();
  }

  @Test
  void saveActivityEnrollment_withoutAuthentication_returns4xx() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    String json =
        """
        {
          "activityId": %d,
          "name": "Luis",
          "surname": "Delgado",
          "nif": "12345678Z",
          "email": "luis@test.com",
          "paid": false
        }
        """
            .formatted(activity.getFirst());

    mockMvc
        .perform(
            post("/clubs/{club}/activityEnrollment", club.slug())
                .with(SecurityMockMvcRequestPostProcessors.anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void saveActivityEnrollment_invalidPayload_returns400() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    String json =
        """
        {
          "activityId": %d,
          "name": "",
          "surname": "Delgado",
          "nif": "12345678A",
          "email": "correo-invalido",
          "paid": false
        }
        """
            .formatted(activity.getFirst());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            post("/clubs/{club}/activityEnrollment", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());
  }
}
