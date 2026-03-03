package luis.delgado.clubmontana.backend.end2end.prices;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
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
class SavePricesTest extends AbstractWebIntegrationTest {

  @Autowired MockMvc mockMvc;

  @Test
  void savePrices_happyPath() throws Exception {
    ClubInserted club = utilTest.insertClub();

    String body =
        """
                [
                  {"title": "Cuota mensual", "price": 15.7},
                  {"title": "Cuota anual", "price": null}
                ]
                """;

    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(
            put("/clubs/{club}/prices", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isNoContent());
  }

  @Test
  void updatePrices_happyPath() throws Exception {
    ClubInserted club = utilTest.insertClub();

    String body =
        """
                [
                  {"title": "Cuota mensual", "price": 15.7},
                  {"title": "Cuota anual", "price": null},
                  {"title": "Inscripcion", "price": 30}
                ]
                """;

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            put("/clubs/{club}/prices", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isNoContent());

    String body2 =
        """
                [
                  {"title": "Cuota mensual", "price": 20},
                  {"title": "Cuota anual", "price": 120}
                ]
                """;

    mockMvc
        .perform(
            put("/clubs/{club}/prices", club.slug())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body2))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get("/clubs/{club}/prices", club.slug()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }
}
