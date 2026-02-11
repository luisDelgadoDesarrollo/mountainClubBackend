package luis.delgado.clubmontana.backend.end2end.doc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SaveDocTest extends AbstractWebIntegrationTest {

  @Autowired MockMvc mockMvc;

  @Test
  void saveDoc_happyPath() throws Exception {
    ClubInserted club = utilTest.insertClub();

    MockMultipartFile file =
        new MockMultipartFile("file", "estatutos.pdf", "application/pdf", "PDF content".getBytes());
    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            multipart(HttpMethod.PUT, "/clubs/{club}/doc?pdfType=BY_LAWS", club.slug()).file(file))
        .andExpect(status().isNoContent());
  }
}
