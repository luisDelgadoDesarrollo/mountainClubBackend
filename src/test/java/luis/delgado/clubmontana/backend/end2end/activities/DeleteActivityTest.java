package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteActivityTest {

  @Autowired private UtilTest utilTest;

  @Autowired private MockMvc mockMvc;

  // todo los dos primeros test no hacen nada baseicamente

  @Test
  void deletePublication_existingPublication_returns204() throws Exception {
    utilTest.mockUserWithClub(1L);
    mockMvc
        .perform(delete("/clubs/{clubId}/activities/{activityId}", 1L, 1L))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublication_notExistingPublication_returns204() throws Exception {
    utilTest.mockUserWithClub(999L);
    mockMvc
        .perform(delete("/clubs/{clubId}/activities/{activityId}", 999L, 999L))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublication_withoutAuthentication_returnsForbidden() throws Exception {

    mockMvc
        .perform(delete("/clubs/{clubId}/activities/{activityId}", 1L, 1L))
        .andExpect(status().isForbidden());
  }
}
