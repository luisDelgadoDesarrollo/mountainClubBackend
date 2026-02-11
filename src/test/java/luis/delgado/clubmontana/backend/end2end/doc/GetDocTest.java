package luis.delgado.clubmontana.backend.end2end.doc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetDocTest extends AbstractWebIntegrationTest {
  @Autowired MockMvc mockMvc;

  @Test
  void getDoc_happyPath() throws Exception {
    ClubInserted clubInserted = utilTest.insertClub();

    MockMultipartFile file =
        new MockMultipartFile("file", "estatutos.pdf", "application/pdf", "PDF content".getBytes());

    utilTest.mockUserWithClub(clubInserted.id());
    // Arrange → guardamos antes
    mockMvc
        .perform(
            multipart("/clubs/{club}/doc?pdfType=BY_LAWS", clubInserted.slug())
                .file(file)
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isNoContent());

    // Act + Assert → GET
    mockMvc
        .perform(get("/clubs/{club}/doc?pdfType=BY_LAWS", clubInserted.slug()))
        .andExpect(status().isOk())
        .andExpect(content().bytes("PDF content".getBytes()));
  }

  @Test
  void getDoc_badType() throws Exception {
    ClubInserted clubInserted = utilTest.insertClub();

    MockMultipartFile file =
        new MockMultipartFile("file", "estatutos.pdf", "application/pdf", "PDF content".getBytes());

    utilTest.mockUserWithClub(clubInserted.id());
    // Arrange → guardamos antes
    mockMvc
        .perform(
            multipart("/clubs/{club}/doc?pdfType=BY_LAWS", clubInserted.slug())
                .file(file)
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get("/clubs/{club}/doc?pdfType=BAD_TYPE", clubInserted.slug()))
        .andExpect(status().isBadRequest());
  }
}
