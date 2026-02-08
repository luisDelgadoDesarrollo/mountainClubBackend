package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ContactRequest {
    String name;
    String email;
    String message;
    String phoneNumber;
}
