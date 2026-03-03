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
class GetPricesTest extends AbstractWebIntegrationTest {

  @Autowired MockMvc mockMvc;

  @Test
  void getPrices_happyPath() throws Exception {
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

    mockMvc
        .perform(get("/clubs/{club}/prices", club.slug()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title").value("Cuota mensual"))
        .andExpect(jsonPath("$[0].price").value(15.70))
        .andExpect(jsonPath("$[1].title").value("Cuota anual"))
        .andExpect(jsonPath("$[1].price").value(0.00));
  }

  @Test
  void getPrices_noFound() throws Exception {
    ClubInserted club = utilTest.insertClub();

    mockMvc
        .perform(get("/clubs/{club}/prices", club.slug()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
