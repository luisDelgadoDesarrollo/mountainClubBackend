package luis.delgado.clubmontana.backend.domain.mails;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.MailAttachment;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;

public interface MailSender {

  void execute(MailMessage mailMessage, List<MailAttachment> files);
}
