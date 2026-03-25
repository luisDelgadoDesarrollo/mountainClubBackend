package luis.delgado.clubmontana.backend.end2end.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailAttachment;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FederationTest extends AbstractWebIntegrationTest {

  @MockitoBean private MailSender mailSender;

  @Test
  void federation_happyPath_returns204_andSendsMail() throws Exception {
    ClubInserted club = utilTest.insertClub();
    MockMultipartFile signUp =
        new MockMultipartFile("signUp", "federation.pdf", "application/pdf", "federation".getBytes());

    mockMvc
        .perform(multipart("/clubs/{club}/contact/federation", club.slug()).file(signUp))
        .andExpect(status().isNoContent());

    ArgumentCaptor<MailMessage> mailCaptor = ArgumentCaptor.forClass(MailMessage.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<MailAttachment>> attachmentCaptor = ArgumentCaptor.forClass(List.class);
    verify(mailSender).execute(mailCaptor.capture(), attachmentCaptor.capture());

    assertThat(mailCaptor.getValue().to()).isEqualTo("delgadofernandez.luis@gmail.com");
    assertThat(mailCaptor.getValue().type()).isEqualTo(MailType.FEDERATION);
    assertThat(attachmentCaptor.getValue()).hasSize(1);
  }
}
