package luis.delgado.clubmontana.backend.unit.useCases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import luis.delgado.clubmontana.backend.application.useCases.ContactUseCasesImpl;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.model.ActivityRegistrationRequest;
import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.domain.model.ClubUser;
import luis.delgado.clubmontana.backend.domain.model.MailAttachment;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.domain.repository.ClubRepository;
import luis.delgado.clubmontana.backend.domain.repository.ClubUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ContactUseCasesActivityTest {

  @Mock private ClubRepository clubRepository;
  @Mock private MailSender mailSender;
  @Mock private ClubUserRepository clubUserRepository;
  @Mock private ActivityRepository activityRepository;

  @InjectMocks private ContactUseCasesImpl contactUseCases;

  private static final Long CLUB_ID = 1L;
  private static final Long ACTIVITY_ID = 10L;
  private static final String CONTACT_EMAIL = "club@montana.es";
  private static final String USER_EMAIL = "user@correo.es";
  private static final String USER_NIF = "12345678A";

  private Activity activity;
  private MultipartFile receipt;

  @BeforeEach
  void setUp() {
    Club club = new Club();
    club.setContactEmail(CONTACT_EMAIL);
    when(clubRepository.getById(CLUB_ID)).thenReturn(club);

    activity = new Activity();
    activity.setTitle("Salida invernal");
    activity.setStartDate(LocalDateTime.of(2026, 2, 20, 9, 30));
    when(activityRepository.getActivity(CLUB_ID, ACTIVITY_ID)).thenReturn(activity);

    receipt =
        new MockMultipartFile("receipt", "receipt.pdf", "application/pdf", "pdf-bytes".getBytes());
  }

  @Test
  void activity_whenFederatedAndClubUser_shouldSendFederatedClubMail() {
    ActivityRegistrationRequest request = buildRequest("FED-001");
    when(clubUserRepository.getUserByClubAndEmailAndNif(CLUB_ID, USER_EMAIL, USER_NIF))
        .thenReturn(new ClubUser());

    contactUseCases.activity(CLUB_ID, ACTIVITY_ID, request, receipt);

    assertMailSent(MailType.ACTIVITY_FEDERATE_CLUB, true);
  }

  @Test
  void activity_whenFederatedAndNoClubUser_shouldSendFederatedNoClubMail() {
    ActivityRegistrationRequest request = buildRequest("FED-001");
    when(clubUserRepository.getUserByClubAndEmailAndNif(CLUB_ID, USER_EMAIL, USER_NIF))
        .thenReturn(null);

    contactUseCases.activity(CLUB_ID, ACTIVITY_ID, request, receipt);

    assertMailSent(MailType.ACTIVITY_FEDERATE_NO_CLUB, true);
  }

  @Test
  void activity_whenNotFederatedAndClubUser_shouldSendNoFederatedClubMail() {
    ActivityRegistrationRequest request = buildRequest(null);
    when(clubUserRepository.getUserByClubAndEmailAndNif(CLUB_ID, USER_EMAIL, USER_NIF))
        .thenReturn(new ClubUser());

    contactUseCases.activity(CLUB_ID, ACTIVITY_ID, request, receipt);

    assertMailSent(MailType.ACTIVITY_NO_FEDERATE_CLUB, false);
  }

  @Test
  void activity_whenNotFederatedAndNoClubUser_shouldSendNoFederatedNoClubMail() {
    ActivityRegistrationRequest request = buildRequest(null);
    when(clubUserRepository.getUserByClubAndEmailAndNif(CLUB_ID, USER_EMAIL, USER_NIF))
        .thenReturn(null);

    contactUseCases.activity(CLUB_ID, ACTIVITY_ID, request, receipt);

    assertMailSent(MailType.ACTIVITY_NO_FEDERATE_NO_CLUB, false);
  }

  private ActivityRegistrationRequest buildRequest(String federateNumber) {
    ActivityRegistrationRequest request = new ActivityRegistrationRequest();
    request.setName("Luis");
    request.setSurname("Delgado");
    request.setNif(USER_NIF);
    request.setEmail(USER_EMAIL);
    request.setFederateNumber(federateNumber);
    return request;
  }

  private void assertMailSent(MailType expectedMailType, boolean expectFederateNumber) {
    ArgumentCaptor<MailMessage> mailCaptor = ArgumentCaptor.forClass(MailMessage.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<MailAttachment>> attachmentCaptor = ArgumentCaptor.forClass(List.class);

    verify(mailSender).execute(mailCaptor.capture(), attachmentCaptor.capture());

    MailMessage mailMessage = mailCaptor.getValue();
    assertThat(mailMessage.to()).isEqualTo(CONTACT_EMAIL);
    assertThat(mailMessage.type()).isEqualTo(expectedMailType);
    assertThat(mailMessage.variables().get("name")).isEqualTo("Luis");
    assertThat(mailMessage.variables().get("surname")).isEqualTo("Delgado");
    assertThat(mailMessage.variables().get("nif")).isEqualTo(USER_NIF);
    assertThat(mailMessage.variables().get("email")).isEqualTo(USER_EMAIL);
    assertThat(mailMessage.variables().get("activityName")).isEqualTo(activity.getTitle());
    assertThat(mailMessage.variables().get("activityDate")).isEqualTo(activity.getStartDate());

    if (expectFederateNumber) {
      assertThat(mailMessage.variables().get("federateNumber")).isEqualTo("FED-001");
    } else {
      assertThat(mailMessage.variables()).doesNotContainKey("federateNumber");
    }

    List<MailAttachment> attachments = attachmentCaptor.getValue();
    assertThat(attachments).hasSize(1);
    assertThat(attachments.getFirst().contentType()).isEqualTo("application/pdf");
  }
}
