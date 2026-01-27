package luis.delgado.clubmontana.backend.domain.mails;

import luis.delgado.clubmontana.backend.domain.model.MailMessage;

public interface MailSender {

  void execute(MailMessage mailMessage) ;
}
