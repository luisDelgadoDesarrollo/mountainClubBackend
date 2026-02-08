package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.ContactRequestDto;
import luis.delgado.clubmontana.backend.domain.model.ContactRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactControllerMapper {
    ContactRequest contactRequestDtoToContactRequest(ContactRequestDto contactRequestDto);
}
