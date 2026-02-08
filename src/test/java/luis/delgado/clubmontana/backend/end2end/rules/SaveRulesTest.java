package luis.delgado.clubmontana.backend.end2end.rules;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SaveRulesTest {
  @Autowired MockMvc mockMvc;
  @Autowired UtilTest utilTest;

  @Test
  void saveRules_happyPath() throws Exception {
    Long clubId = utilTest.insertClub();

    String body =
        """
                [
                  "No tirar basura",
                  "Respetar el entorno",
                  "Seguir las indicaciones del guía"
                ]
                """;

    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(
            put("/clubs/{clubId}/rules", clubId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isNoContent());
  }
}
