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
      "execution(* *..*UseCase.*(Long, ..)) "
          + " && !@annotation(luis.delgado.clubmontana.backend.core.security.NoAuthenticationNeeded)")
  public void checkClubAuthorization(JoinPoint joinPoint) {

    Object[] args = joinPoint.getArgs();

    // Seguridad extra
    if (args.length == 0 || !(args[0] instanceof Long clubIdFromMethod)) {
      return;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no autenticado");
    }
    if (!clubIdFromMethod.equals(user.getClubId())) {
      log.warn(
          "Access denied: user={} userClub={} requestedClub={}",
          user.getUsername(),
          user.getClubId(),
          clubIdFromMethod);

      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No pertenece a este club");
    }
  }
}
