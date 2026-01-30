package luis.delgado.clubmontana.backend.end2end.publications;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;
import luis.delgado.clubmontana.backend.api.mappers.PublicationControllerMapper;
import luis.delgado.clubmontana.backend.application.services.FileSystemImageStorageService;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreatePublicationTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private PublicationUseCases publicationUseCases;

  @Autowired private FileSystemImageStorageService fileSystemImageStorageService;

  @MockitoBean private PublicationControllerMapper publicationControllerMapper;

  @Test
  void createPublication_happyPath() throws Exception {

    String json =
        """
        {
          "title": "Mi publicación",
          "text": "Texto",
          "images": [
            { "image": "img-1", "description": "foto 1" }
          ],
          "links": []
        }
        """;

    MockMultipartFile data =
        new MockMultipartFile(
            "data", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image =
        new MockMultipartFile(
            "img-1", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "fake-image".getBytes());

    Publication publication = new Publication();
    publication.setPublicationId(1L);
    publication.setImages(List.of());
    publication.setLinks(List.of());

    when(publicationControllerMapper.publicationRequestDtoToCreatePublicationCommand(any()))
        .thenReturn(publication);

    when(publicationUseCases.create(
            eq(1L), any(Publication.class), ArgumentMatchers.<String, MultipartFile>anyMap()))
        .thenReturn(publication);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    mockMvc
        .perform(
            multipart("/publications/{clubId}", 1L)
                .file(data)
                .file(image)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(authentication(authentication)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  void createPublication_withoutToken_shouldReturn401() throws Exception {
    MockMultipartFile data =
        new MockMultipartFile("data", "data", MediaType.APPLICATION_JSON_VALUE, "{}".getBytes());

    mockMvc
        .perform(multipart("/publications/{clubId}", 1L).file(data))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createPublication_invalidPayload_shouldReturn400() throws Exception {
    String invalidJson =
        """
                {
                  "text": "sin título"
                }
                """;

    MockMultipartFile data =
        new MockMultipartFile(
            "data", "data", MediaType.APPLICATION_JSON_VALUE, invalidJson.getBytes());

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    mockMvc
        .perform(
            multipart("/publications/{clubId}", 1L).file(data).with(authentication(authentication)))
        .andExpect(status().is4xxClientError());
  }
}
