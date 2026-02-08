package luis.delgado.clubmontana.backend.application.useCases;

import java.util.HashMap;
import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.Club;
import luis.delgado.clubmontana.backend.domain.model.ContactRequest;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.domain.repository.ClubRepository;
import luis.delgado.clubmontana.backend.domain.userCases.ContactUseCases;

@UseCase
public class ContactUseCasesImpl implements ContactUseCases {

    private final ClubRepository clubRepository;
    private final MailSender mailSender;

    public ContactUseCasesImpl(ClubRepository clubRepository, MailSender mailSender) {
        this.clubRepository = clubRepository;
        this.mailSender = mailSender;
    }

    @NoAuthenticationNeeded
    @Override
    public void contact(Long clubId, ContactRequest contactRequest) {
        Club club = clubRepository.getById(clubId);
        Map<String, Object> params = new HashMap<>();
        params.put("email", contactRequest.getEmail());
        params.put("name", contactRequest.getName());
        params.put("message", contactRequest.getMessage());
        params.put("phoneNumber", contactRequest.getPhoneNumber());
        mailSender.execute(new MailMessage(club.getContactEmail(), MailType.CONTACT_REQUEST, params));
    }
}
