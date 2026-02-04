package luis.delgado.clubmontana.backend.end2end.rules;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GetRulesTest {
  @Autowired MockMvc mockMvc;
  @Autowired JdbcTemplate jdbcTemplate;

  private Long insertClub() {
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
                                                            has_hazte_socio
                                                          ) VALUES (
                                                            ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,
                                                            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
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

          return ps;
        },
        keyHolder);

    return keyHolder.getKey().longValue();
  }

  @Test
  void getRules_happyPath() throws Exception {
    Long clubId = insertClub();

    String body =
        """
                [
                  "No tirar basura",
                  "Respetar el entorno"
                ]
                """;

    UtilTest.mockUserWithClub(clubId);

    // Arrange → guardamos primero
    mockMvc
        .perform(
            put("/rules/{clubId}", clubId).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isNoContent());

    // Act + Assert
    mockMvc
        .perform(get("/rules/{clubId}", clubId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].ruleId").value(1))
        .andExpect(jsonPath("$[0].rule").value("No tirar basura"))
        .andExpect(jsonPath("$[1].ruleId").value(2))
        .andExpect(jsonPath("$[1].rule").value("Respetar el entorno"));
  }
}
