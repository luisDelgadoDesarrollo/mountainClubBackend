package luis.delgado.clubmontana.backend.end2end.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContactMessageTest extends AbstractWebIntegrationTest {

  @MockitoBean private MailSender mailSender;

  @Test
  void contact_happyPath_returns204_andSendsMail() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String json =
        """
        {
          "name": "Luis",
          "email": "luis@test.com",
          "message": "Quiero informacion sobre el club",
          "phoneNumber": "+34600123123"
        }
        """;

    mockMvc
        .perform(
            post("/clubs/{club}/contact/message", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isNoContent());

    ArgumentCaptor<MailMessage> mailCaptor = ArgumentCaptor.forClass(MailMessage.class);
    verify(mailSender).execute(mailCaptor.capture(), isNull());

    MailMessage mailMessage = mailCaptor.getValue();
    assertThat(mailMessage.to()).isEqualTo("delgadofernandez.luis@gmail.com");
    assertThat(mailMessage.type()).isEqualTo(MailType.CONTACT_REQUEST);
    assertThat(mailMessage.variables().get("name")).isEqualTo("Luis");
    assertThat(mailMessage.variables().get("email")).isEqualTo("luis@test.com");
    assertThat(mailMessage.variables().get("message")).isEqualTo("Quiero informacion sobre el club");
  }

  @Test
  void contact_invalidPayload_returns400() throws Exception {
    ClubInserted club = utilTest.insertClub();
    String json =
        """
        {
          "name": "",
          "email": "correo-invalido",
          "message": ""
        }
        """;

    mockMvc
        .perform(
            post("/clubs/{club}/contact/message", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());
  }
}
