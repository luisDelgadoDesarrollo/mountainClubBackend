package luis.delgado.clubmontana.backend.end2end.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailAttachment;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEnrollmentEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubUserEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ids.ActivityEnrollmentIdEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEnrollmentEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubUserEntityJpa;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ActivityContactTest extends AbstractWebIntegrationTest {

  private static final String USER_EMAIL = "luis@test.com";
  private static final String USER_NIF = "12345678Z";

  @Autowired private ActivityEnrollmentEntityJpa activityEnrollmentEntityJpa;
  @Autowired private ActivityEntityJpa activityEntityJpa;
  @Autowired private ClubUserEntityJpa clubUserEntityJpa;
  @MockitoBean private MailSender mailSender;

  @Test
  void activity_happyPath_returns204_persistsEnrollment_andSendsMail() throws Exception {
    ClubInserted club = utilTest.insertClub();
    insertClubUser(club.id(), USER_EMAIL, USER_NIF);
    Pair<Long, String> activity = utilTest.createActivity(club);
    var activityEntity = activityEntityJpa.findById(activity.getFirst()).orElseThrow();
    activityEntity.setMaxParticipants(20);
    activityEntityJpa.save(activityEntity);

    MockPart data =
        new MockPart(
            "data",
            """
            {
              "name": "Luis",
              "surname": "Delgado",
              "nif": "12345678Z",
              "email": "luis@test.com",
              "federateNumber": "FED-001"
            }
            """
                .getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile receipt =
        new MockMultipartFile("receipt", "receipt.pdf", "application/pdf", "receipt".getBytes());

    mockMvc
        .perform(
            multipart("/clubs/{club}/contact/activity/{activity}", club.slug(), activity.getSecond())
                .part(data)
                .file(receipt))
        .andExpect(status().isNoContent());

    ActivityEnrollmentIdEntity id = new ActivityEnrollmentIdEntity(activity.getFirst(), USER_NIF);
    ActivityEnrollmentEntity saved = activityEnrollmentEntityJpa.findById(id).orElse(null);

    assertThat(saved).isNotNull();
    assertThat(saved.getEmail()).isEqualTo(USER_EMAIL);
    assertThat(saved.getName()).isEqualTo("Luis");
    assertThat(saved.getPaid()).isFalse();

    ArgumentCaptor<MailMessage> mailCaptor = ArgumentCaptor.forClass(MailMessage.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<MailAttachment>> attachmentCaptor = ArgumentCaptor.forClass(List.class);
    verify(mailSender).execute(mailCaptor.capture(), attachmentCaptor.capture());

    assertThat(mailCaptor.getValue().type()).isEqualTo(MailType.ACTIVITY_FEDERATE_CLUB);
    assertThat(attachmentCaptor.getValue()).hasSize(1);
  }

  @Test
  void activity_whenFull_returns409() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    var activityEntity = activityEntityJpa.findById(activity.getFirst()).orElseThrow();
    activityEntity.setMaxParticipants(1);
    activityEntityJpa.save(activityEntity);
    activityEnrollmentEntityJpa.save(
        ActivityEnrollmentEntity.builder()
            .activityId(activity.getFirst())
            .nif("11111111H")
            .name("Ana")
            .surname("Perez")
            .email("ana@test.com")
            .paid(false)
            .createdAt(LocalDateTime.now())
            .build());

    MockPart data =
        new MockPart(
            "data",
            """
            {
              "name": "Luis",
              "surname": "Delgado",
              "nif": "12345678Z",
              "email": "luis@test.com"
            }
            """
                .getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile receipt =
        new MockMultipartFile("receipt", "receipt.pdf", "application/pdf", "receipt".getBytes());

    mockMvc
        .perform(
            multipart("/clubs/{club}/contact/activity/{activity}", club.slug(), activity.getSecond())
                .part(data)
                .file(receipt))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error").value("ACTIVITY_FULL"));

    assertThat(activityEnrollmentEntityJpa.countByActivityId(activity.getFirst())).isEqualTo(1);
    verify(mailSender, never()).execute(any(), any());
  }

  @Test
  void activity_invalidPayload_returns400() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    var activityEntity = activityEntityJpa.findById(activity.getFirst()).orElseThrow();
    activityEntity.setMaxParticipants(20);
    activityEntityJpa.save(activityEntity);

    MockPart data =
        new MockPart(
            "data",
            """
            {
              "name": "",
              "surname": "Delgado",
              "nif": "12345678Z",
              "email": "correo-invalido"
            }
            """
                .getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile receipt =
        new MockMultipartFile("receipt", "receipt.pdf", "application/pdf", "receipt".getBytes());

    mockMvc
        .perform(
            multipart("/clubs/{club}/contact/activity/{activity}", club.slug(), activity.getSecond())
                .part(data)
                .file(receipt))
        .andExpect(status().isBadRequest());
  }

  private void insertClubUser(Long clubId, String email, String nif) {
    clubUserEntityJpa.save(
        ClubUserEntity.builder()
            .clubId(clubId)
            .email(email)
            .nif(nif)
            .name("Luis")
            .surname("Delgado")
            .federatedNumber("FED-001")
            .birthDate(LocalDate.of(1990, 1, 10))
            .address("Calle Mayor 1")
            .city("Madrid")
            .state("Madrid")
            .postalCode("28001")
            .phone("600123123")
            .homePhone("910000000")
            .build());
  }
}
