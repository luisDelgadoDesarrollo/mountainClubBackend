package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.ContactRequest;

public interface ContactUseCases {
    void contact(Long clubId, ContactRequest contactRequest);
}
