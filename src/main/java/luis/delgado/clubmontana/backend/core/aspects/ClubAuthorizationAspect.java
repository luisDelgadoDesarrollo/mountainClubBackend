package luis.delgado.clubmontana.backend.core.aspects;

import lombok.extern.slf4j.Slf4j;
import luis.delgado.clubmontana.backend.domain.model.CustomUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Aspect
@Component
public class ClubAuthorizationAspect {
  @Before(
      "@within(luis.delgado.clubmontana.backend.core.annotations.UseCase) "
          + "&& execution(* *(..)) "
          + "&& args(clubId, ..) "
          + "&& !@annotation(luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded)"
          + "&& !@within(luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded)")
  public void checkClubAuthorization(JoinPoint joinPoint, Long clubId) {

    Object[] args = joinPoint.getArgs();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no autenticado");
    }
    if (!clubId.equals(user.getClubId())) {
      log.warn(
          "Access denied: user={} userClub={} requestedClub={}",
          user.getUsername(),
          user.getClubId(),
          clubId);

      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No pertenece a este club");
    }
  }
}
