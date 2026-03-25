package luis.delgado.clubmontana.backend.end2end.activityEnrollment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEnrollmentEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEnrollmentEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GetActivityEnrollmentsTest extends AbstractWebIntegrationTest {

  @Autowired private ActivityEnrollmentEntityJpa activityEnrollmentEntityJpa;

  @Test
  void getActivityEnrollments_happyPath_returnsEnrollments() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    activityEnrollmentEntityJpa.save(
        ActivityEnrollmentEntity.builder()
            .activityId(activity.getFirst())
            .nif("12345678Z")
            .name("Luis")
            .surname("Delgado")
            .email("luis@test.com")
            .paid(false)
            .createdAt(LocalDateTime.now())
            .build());
    activityEnrollmentEntityJpa.save(
        ActivityEnrollmentEntity.builder()
            .activityId(activity.getFirst())
            .nif("11111111H")
            .name("Ana")
            .surname("Perez")
            .email("ana@test.com")
            .paid(true)
            .createdAt(LocalDateTime.now())
            .build());

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(get("/clubs/{club}/activityEnrollment/{activity}", club.slug(), activity.getSecond()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].activityId").value(activity.getFirst()))
        .andExpect(jsonPath("$[0].name").exists())
        .andExpect(jsonPath("$[0].surname").exists())
        .andExpect(jsonPath("$[0].nif").exists())
        .andExpect(jsonPath("$[0].email").exists())
        .andExpect(jsonPath("$[0].paid").exists());
  }

  @Test
  void getActivityEnrollments_whenNoEnrollments_returnsEmptyList() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(get("/clubs/{club}/activityEnrollment/{activity}", club.slug(), activity.getSecond()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getActivityEnrollments_withoutAuthentication_returns4xx() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);

    mockMvc
        .perform(
            get("/clubs/{club}/activityEnrollment/{activity}", club.slug(), activity.getSecond())
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().is4xxClientError());
  }
}
