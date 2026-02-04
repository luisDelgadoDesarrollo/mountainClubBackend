package luis.delgado.clubmontana.backend.end2end;

import luis.delgado.clubmontana.backend.domain.model.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UtilTest {

  public static void mockUserWithClub(Long clubId) {
    CustomUserDetails user = new CustomUserDetails("email@test", 1L, clubId);

    Authentication auth =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
