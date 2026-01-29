package luis.delgado.clubmontana.backend.application.useCases;

import java.util.Map;
import java.util.Optional;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.mails.MailSender;
import luis.delgado.clubmontana.backend.domain.model.MailMessage;
import luis.delgado.clubmontana.backend.domain.model.User;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserRequest;
import luis.delgado.clubmontana.backend.domain.model.commands.createUser.CreateUserResponse;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;
import luis.delgado.clubmontana.backend.domain.repository.UserRepository;
import luis.delgado.clubmontana.backend.domain.services.CreateUserService;
import luis.delgado.clubmontana.backend.domain.userCases.UserUseCases;

@UseCase
public class UserUseCasesImpl implements UserUseCases {

  private final UserRepository userRepository;
  private final MailSender mailSender;
  private final CreateUserService createUserService;

  public UserUseCasesImpl(
      UserRepository userRepository, MailSender mailSender, CreateUserService createUserService) {
    this.userRepository = userRepository;
    this.mailSender = mailSender;
    this.createUserService = createUserService;
  }

  @Override
  public CreateUserResponse createUserUseCase(CreateUserRequest createUserRequest) {
    Optional<User> userOptional = userRepository.getUserByEmail(createUserRequest.getEmail());
    if (userOptional.isEmpty()) {
      User execute = createUserService.execute(createUserRequest);
      CreateUserResponse createUserResponse = CreateUserResponse.fromUser(execute, "Creado");
      mailSender.execute(
          new MailMessage(createUserResponse.getEmail(), MailType.USER_CREATED, Map.of()));
      return createUserResponse;
    }
    User user = userOptional.get();
    if (!user.getEmailVerified()) {
      CreateUserResponse createUserResponse =
          CreateUserResponse.fromUser(user, "Usuario no verificado");
      mailSender.execute(
          new MailMessage(createUserResponse.getEmail(), MailType.USER_CREATED, Map.of()));
      return createUserResponse;
    }

    return CreateUserResponse.fromUser(user, "Usuario usable");
  }
}
