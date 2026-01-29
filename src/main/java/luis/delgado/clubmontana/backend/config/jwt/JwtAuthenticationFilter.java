package luis.delgado.clubmontana.backend.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import luis.delgado.clubmontana.backend.domain.model.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    if (jwtService.isTokenValid(token)
        && SecurityContextHolder.getContext().getAuthentication() == null) {

      String email = jwtService.extractEmail(token);
      Long userId = jwtService.extractUserId(token);
      Long clubId = jwtService.extractClubId(token);

      CustomUserDetails principal = new CustomUserDetails(email, userId, clubId);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

      log.debug("JWT authenticated user={}", email);
    }

    filterChain.doFilter(request, response);
  }
}
