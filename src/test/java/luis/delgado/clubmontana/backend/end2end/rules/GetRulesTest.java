package luis.delgado.clubmontana.backend.end2end.rules;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
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
class GetRulesTest extends AbstractWebIntegrationTest {
  @Autowired MockMvc mockMvc;

  @Test
  void getRules_happyPath() throws Exception {
    Long clubId = utilTest.insertClub();

    String body =
        """
                [
                  "No tirar basura",
                  "Respetar el entorno"
                ]
                """;

    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(
            put("/clubs/{clubId}/rules", clubId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isNoContent());

    // Act + Assert
    mockMvc
        .perform(get("/clubs/{clubId}/rules", clubId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].ruleId").value(1))
        .andExpect(jsonPath("$[0].rule").value("No tirar basura"))
        .andExpect(jsonPath("$[1].ruleId").value(2))
        .andExpect(jsonPath("$[1].rule").value("Respetar el entorno"));
  }

  @Test
  void getRules_noFound() throws Exception {
    Long clubId = utilTest.insertClub();

    // Act + Assert
    mockMvc.perform(get("/clubs/{clubId}/rules", clubId)).andExpect(status().isOk());
  }
}
