package luis.delgado.clubmontana.backend.end2end.publications;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeletePublicationTest {

    @Autowired
    private MockMvc mockMvc;

    private Authentication userAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                "user-1",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void deletePublication_existingPublication_returns204() throws Exception {

        mockMvc
                .perform(
                        delete("/publications/{clubId}/{publicationId}", 1L, 1L)
                                .with(authentication(userAuthentication())))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePublication_notExistingPublication_returns204() throws Exception {

        mockMvc
                .perform(
                        delete("/publications/{clubId}/{publicationId}", 999L, 999L)
                                .with(authentication(userAuthentication())))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePublication_withoutAuthentication_returnsForbidden() throws Exception {

        mockMvc
                .perform(
                        delete("/publications/{clubId}/{publicationId}", 1L, 1L))
                .andExpect(status().isForbidden());
    }
}

