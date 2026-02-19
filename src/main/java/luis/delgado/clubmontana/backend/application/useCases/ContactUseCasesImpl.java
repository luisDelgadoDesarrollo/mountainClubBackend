package luis.delgado.clubmontana.backend.application.useCases;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.*;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.domain.repository.ClubRepository;
import luis.delgado.clubmontana.backend.domain.repository.ClubUserRepository;
import luis.delgado.clubmontana.backend.domain.userCases.ContactUseCases;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@UseCase
@NoAuthenticationNeeded
public class ContactUseCasesImpl implements ContactUseCases {

  private final ClubRepository clubRepository;
  private final MailSender mailSender;
  private final ClubUserRepository clubUserRepository;
  private final ActivityRepository activityRepository;

  public ContactUseCasesImpl(
      ClubRepository clubRepository,
      MailSender mailSender,
      ClubUserRepository clubUserRepository,
      ActivityRepository activityRepository) {
    this.clubRepository = clubRepository;
    this.mailSender = mailSender;
    this.clubUserRepository = clubUserRepository;
    this.activityRepository = activityRepository;
  }

  @Override
  public void contact(Long clubId, ContactRequest contactRequest) {
    Club club = clubRepository.getById(clubId);
    Map<String, Object> params = new HashMap<>();
    params.put("email", contactRequest.getEmail());
    params.put("name", contactRequest.getName());
    params.put("message", contactRequest.getMessage());
    params.put("phoneNumber", contactRequest.getPhoneNumber());
    mailSender.execute(
        new MailMessage(club.getContactEmail(), MailType.CONTACT_REQUEST, params), null);
  }

  @Override
  public void memberShipSingup(Long clubId, MultipartFile signUp, MultipartFile receipt) {
    Club club = clubRepository.getById(clubId);
    List<MailAttachment> mailAttachments;
    try {
      mailAttachments =
          List.of(
              new MailAttachment(signUp.getName(), signUp.getBytes(), signUp.getContentType()),
              new MailAttachment(receipt.getName(), receipt.getBytes(), receipt.getContentType()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    mailSender.execute(
        new MailMessage(club.getContactEmail(), MailType.MEMBERSHIP_SIGNUP, Map.of()),
        mailAttachments);
  }

  @Override
  public void federation(Long clubId, MultipartFile dataResponsibility) {
    Club club = clubRepository.getById(clubId);
    List<MailAttachment> mailAttachments;
    try {
      mailAttachments =
          List.of(
              new MailAttachment(
                  dataResponsibility.getName(),
                  dataResponsibility.getBytes(),
                  dataResponsibility.getContentType()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    mailSender.execute(
        new MailMessage(club.getContactEmail(), MailType.FEDERATION, Map.of()), mailAttachments);
  }

  // todo repasar por que puede ser que falle el usuario al rellenar datos, y se envie un email
  // incorrecto asi como enviar un numero de federado falso
  @Override
  public void activity(
      Long clubId,
      Long activityId,
      ActivityRegistrationRequest activityRegistrationRequest,
      MultipartFile receipt) {
    Club club = clubRepository.getById(clubId);
    Activity activity = activityRepository.getActivity(clubId, activityId);
    ClubUser clubUser =
        clubUserRepository.getUserByClubAndEmailAndNif(
            clubId, activityRegistrationRequest.getEmail(), activityRegistrationRequest.getNif());
    Map<String, Object> params = new HashMap<>();
    params.put("nif", activityRegistrationRequest.getNif());
    params.put("email", activityRegistrationRequest.getEmail());
    params.put("name", activityRegistrationRequest.getName());
    params.put("surname", activityRegistrationRequest.getSurname());
    params.put("activityName", activity.getTitle());
    params.put("activityDate", activity.getStartDate());
    List<MailAttachment> mailAttachments;
    try {
      mailAttachments =
          List.of(
              new MailAttachment(receipt.getName(), receipt.getBytes(), receipt.getContentType()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (StringUtils.hasText(activityRegistrationRequest.getFederateNumber())) {
      params.put("federateNumber", activityRegistrationRequest.getFederateNumber());
      if (clubUser != null) {

        mailSender.execute(
            new MailMessage(club.getContactEmail(), MailType.ACTIVITY_FEDERATE_CLUB, params),
            mailAttachments);
      } else {
        mailSender.execute(
            new MailMessage(club.getContactEmail(), MailType.ACTIVITY_FEDERATE_NO_CLUB, params),
            mailAttachments);
      }
    } else {
      if (clubUser != null) {
        mailSender.execute(
            new MailMessage(club.getContactEmail(), MailType.ACTIVITY_NO_FEDERATE_CLUB, params),
            mailAttachments);
      } else {
        mailSender.execute(
            new MailMessage(club.getContactEmail(), MailType.ACTIVITY_NO_FEDERATE_NO_CLUB, params),
            mailAttachments);
      }
    }
  }
}
