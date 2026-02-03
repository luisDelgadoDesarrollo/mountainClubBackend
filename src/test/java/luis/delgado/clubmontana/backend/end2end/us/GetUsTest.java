package luis.delgado.clubmontana.backend.end2end.us;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GetUsTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldGetUsByClubId() throws Exception {
    Long clubId = 1L;

    mockMvc
        .perform(get("/us/{clubId}", clubId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clubId").value(clubId))
        .andExpect(jsonPath("$.text").isNotEmpty())
        .andExpect(jsonPath("$.images").isArray());
  }

  @Test
  void shouldReturn404WhenClubDoesNotExist() throws Exception {
    Long clubId = 9999L;

    mockMvc
        .perform(get("/clubs/{clubId}/us", clubId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }
}
