package luis.delgado.clubmontana.backend.end2end.auth;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LogoutTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void logout_returns_204_and_clears_refresh_cookie() throws Exception {
    mockMvc
        .perform(post("/auth/logout").cookie(new Cookie("refresh_token", "fake-token")))
        .andExpect(status().isNoContent())
        .andExpect(header().string("Set-Cookie", containsString("refresh_token=")))
        .andExpect(header().string("Set-Cookie", containsString("Max-Age=0")))
        .andExpect(header().string("Set-Cookie", containsString("Path=/")))
        .andExpect(header().string("Set-Cookie", containsString("HttpOnly")))
        .andExpect(header().string("Set-Cookie", containsString("SameSite=Lax")));
  }
}
