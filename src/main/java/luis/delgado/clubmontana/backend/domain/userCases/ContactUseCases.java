package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.ContactRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ContactUseCases {
  void contact(Long clubId, ContactRequest contactRequest);

  void memberShipSingup(Long clubId, MultipartFile signUp, MultipartFile receipt);
}
