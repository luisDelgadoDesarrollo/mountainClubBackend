package luis.delgado.clubmontana.backend.end2end;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;
import luis.delgado.clubmontana.backend.domain.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UtilTest {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void mockUserWithClub(Long clubId) {
    CustomUserDetails user = new CustomUserDetails("email@test", 1L, clubId);

    Authentication auth =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public Long insertClub() {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  """
                                                                INSERT INTO club (
                                                                  name,
                                                                  nif,
                                                                  description,
                                                                  logo,
                                                                  url,
                                                                  created_at,
                                                                  created_by,
                                                                  has_inicio,
                                                                  has_secciones,
                                                                  has_galeria,
                                                                  has_enlaces,
                                                                  has_contacto,
                                                                  has_federarse,
                                                                  has_tienda,
                                                                  has_calendario,
                                                                  has_conocenos,
                                                                  has_noticias,
                                                                  has_foro,
                                                                  has_estatutos,
                                                                  has_normas,
                                                                  has_hazte_socio,
                                                                contact_email
                                                                ) VALUES (
                                                                  ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,
                                                                  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                                                                )
                                                                """,
                  Statement.RETURN_GENERATED_KEYS);

          String suffix = UUID.randomUUID().toString().substring(0, 8);

          ps.setString(1, "Club Test");
          ps.setString(2, "G" + suffix); // nif único
          ps.setString(3, "Club de prueba");
          ps.setString(4, "logo.png");
          ps.setString(5, "club-" + suffix + ".es"); // url única
          ps.setLong(6, 1L); // created_by

          ps.setBoolean(7, true); // has_inicio
          ps.setBoolean(8, true); // has_secciones
          ps.setBoolean(9, false);
          ps.setBoolean(10, false);
          ps.setBoolean(11, false);
          ps.setBoolean(12, false);
          ps.setBoolean(13, false);
          ps.setBoolean(14, false);
          ps.setBoolean(15, false);
          ps.setBoolean(16, false);
          ps.setBoolean(17, false);
          ps.setBoolean(18, false);
          ps.setBoolean(19, false);
          ps.setBoolean(20, false);
          ps.setString(21, "delgadofernandez.luis@gmail.com");

          return ps;
        },
        keyHolder);

    return keyHolder.getKey().longValue();
  }
}
