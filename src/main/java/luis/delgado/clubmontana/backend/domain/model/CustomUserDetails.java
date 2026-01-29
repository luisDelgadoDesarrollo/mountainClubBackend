package luis.delgado.clubmontana.backend.domain.model;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
  private final String email;
  @Getter private final Long userId;
  @Getter private final Long clubId;

  public CustomUserDetails(String email, Long userId, Long clubId) {
    this.email = email;
    this.userId = userId;
    this.clubId = clubId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
