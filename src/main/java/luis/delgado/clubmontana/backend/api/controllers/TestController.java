package luis.delgado.clubmontana.backend.api.controllers;

import java.util.Map;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

  @Autowired MailSender mailSender;

  @GetMapping("/mailTest/{email}")
  public void sendEmail(@PathVariable String email) {
    mailSender.execute(
        new MailMessage(
            email,
            MailType.TEST,
            Map.of(
                "username",
                "Luis",
                "activationUrl",
                "https://misbarrancos.com/activate?token=abc123")));
  }
}
