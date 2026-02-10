package luis.delgado.clubmontana.backend.end2end.doc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
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
    Long clubId = 1L;

    MockMultipartFile file =
        new MockMultipartFile("file", "estatutos.pdf", "application/pdf", "PDF content".getBytes());

    utilTest.mockUserWithClub(clubId);
    // Arrange → guardamos antes
    mockMvc
        .perform(
            multipart("/clubs/{clubId}/doc?pdfType=BY_LAWS", clubId)
                .file(file)
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isNoContent());

    // Act + Assert → GET
    mockMvc
        .perform(get("/clubs/{clubId}/doc?pdfType=BY_LAWS", clubId))
        .andExpect(status().isOk())
        .andExpect(content().bytes("PDF content".getBytes()));
  }

  @Test
  void getDoc_badType() throws Exception {
    Long clubId = utilTest.insertClub();

    MockMultipartFile file =
        new MockMultipartFile("file", "estatutos.pdf", "application/pdf", "PDF content".getBytes());

    utilTest.mockUserWithClub(clubId);
    // Arrange → guardamos antes
    mockMvc
        .perform(
            multipart("/clubs/{clubId}/doc?pdfType=BY_LAWS", clubId)
                .file(file)
                .with(
                    request -> {
                      request.setMethod("PUT");
                      return request;
                    }))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get("/clubs/{clubId}/doc?pdfType=BAD_TYPE", clubId))
        .andExpect(status().isBadRequest());
  }
}
