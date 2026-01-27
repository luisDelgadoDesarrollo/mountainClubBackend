package luis.delgado.clubmontana.backend.application.useCases;

import java.util.Map;
import java.util.Optional;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserCommand;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserResponse;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.domain.repository.GetUserByEmailRepository;
import luis.delgado.clubmontana.backend.domain.repository.SaveUserRepository;
import luis.delgado.clubmontana.backend.domain.services.CreateUser;
import luis.delgado.clubmontana.backend.domain.userCases.UserUseCases;
import org.springframework.stereotype.Service;

@Service
public class UserUseCasesImpl implements UserUseCases {

  private final GetUserByEmailRepository getUserByEmailRepository;
  private final SaveUserRepository saveUserRepository;
  private final MailSender mailSender;
  private final CreateUser createUser;

  public UserUseCasesImpl(
      GetUserByEmailRepository getUserByEmailRepository,
      SaveUserRepository saveUserRepository,
      MailSender mailSender,
      CreateUser createUser) {
    this.getUserByEmailRepository = getUserByEmailRepository;
    this.saveUserRepository = saveUserRepository;
    this.mailSender = mailSender;
    this.createUser = createUser;
  }

  @Override
  public CreateUserResponse createUserUseCase(CreateUserCommand createUserCommand) {
    Optional<User> userOptional = getUserByEmailRepository.execute(createUserCommand.email());
    if (userOptional.isEmpty()) {
      User execute = createUser.execute(createUserCommand);
      CreateUserResponse createUserResponse = CreateUserResponse.fromUser(execute, "Creado");
      mailSender.execute(
          new MailMessage(createUserResponse.email(), MailType.USER_CREATED, Map.of()));
      return createUserResponse;
    }
    User user = userOptional.get();
    if (!user.getEmailVerified()) {
      CreateUserResponse createUserResponse =
          CreateUserResponse.fromUser(user, "Usuario no verificado");
      mailSender.execute(
          new MailMessage(createUserResponse.email(), MailType.USER_CREATED, Map.of()));
      return createUserResponse;
    }
    if (!user.getActive()) {
      user.setActive(true);
      User userSaved = saveUserRepository.execute(user);
      return CreateUserResponse.fromUser(userSaved, "Usuario reactivado");
    }

    return CreateUserResponse.fromUser(user, "Usuario usable");
  }
}
