package luis.delgado.clubmontana.backend.infrastructure.mails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Log4j2
@Component
public class MailSenderImpl implements MailSender {

  private final JavaMailSender javaMailSender;
  private final TemplateEngine templateEngine;

  public MailSenderImpl(
      JavaMailSender javaMailSender,
      TemplateEngine templateEngine,
      JavaMailSender javaMailSender1,
      TemplateEngine templateEngine1) {
    this.javaMailSender = javaMailSender1;
    this.templateEngine = templateEngine1;
  }

  @Override
  @Async
  public void execute(MailMessage mailMessage) {
    String template = resolveTemplate(mailMessage.type());
    String subject = resolveSubject(mailMessage.type());

    Context context = new Context();
    context.setVariables(mailMessage.variables());
    try {
        String htmlBody = templateEngine.process(template, context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper =
            new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(mailMessage.to());
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("no-reply@clubmontana.es");
        log.info(mimeMessage.toString());
        javaMailSender.send(mimeMessage);

    } catch (MessagingException e) {
      log.warn(
          "Error sending email to {} of type {}. Ignoring.",
          mailMessage.to(),
          mailMessage.type(),
          e);
    }
  }

  private String resolveTemplate(MailType type) {
    return switch (type) {
      case USER_CREATED -> "mail/templates/user-created.html";
      case USER_REACTIVATED -> "mail/templates/user-reactivated.html";
      case PASSWORD_RESET -> "mail/templates/password-reset.html";
    };
  }

  private String resolveSubject(MailType type) {
    return switch (type) {
      case USER_CREATED -> "Verifica tu cuenta";
      case USER_REACTIVATED -> "Tu cuenta ha sido reactivada";
      case PASSWORD_RESET -> "Restablece tu contraseña";
    };
  }
}
