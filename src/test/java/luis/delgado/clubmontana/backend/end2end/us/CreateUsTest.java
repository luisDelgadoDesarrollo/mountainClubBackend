package luis.delgado.clubmontana.backend.end2end.us;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UsEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UsEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CreateUsTest extends AbstractWebIntegrationTest {

  FileSystemFileStorageService service;
  @Autowired private MockMvc mockMvc;
  @Autowired private UsEntityJpa usEntityJpa;

  @Test
  void shouldCreateUsWithImagesAndFiles() throws Exception {
    ClubInserted club = utilTest.insertClub();

    // ---------- multipart files ----------
    MockMultipartFile file1 =
        new MockMultipartFile(
            "files",
            "image1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    // ---------- UsRequestDto como JSON ----------
    String usRequestJson =
        """
        {
          "text": "Texto de prueba",
          "images": [
            { "image": "image1.jpg" }
          ]
        }
        """;

    MockPart data = new MockPart("us", usRequestJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(
            multipart("/clubs/{club}/us", club.slug())
                .part(data)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());

    Optional<UsEntity> usEntityOptional = usEntityJpa.findById(club.id());
    assertThat(usEntityOptional.isPresent()).isTrue();
    UsEntity usEntity = usEntityOptional.get();
    assertThat(usEntity.getClubId()).isEqualTo(club.id());
    assertThat(usEntity.getText()).isEqualTo("Texto de prueba");

    Optional<UsEntity> images = usEntityJpa.findById(usEntity.getClubId());
    assertThat(images).isPresent();
    assertThat(images.get().getImages()).hasSize(1);
  }

  @Test
  void createUs_withoutAuthorization() throws Exception {
    ClubInserted club = utilTest.insertClub();

    // ---------- multipart files ----------
    MockMultipartFile file1 =
        new MockMultipartFile(
            "files",
            "image1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    // ---------- UsRequestDto como JSON ----------
    String usRequestJson =
        """
            {
              "text": "Texto de prueba",
              "images": [
                { "image": "image1.jpg" }
              ]
            }
            """;

    MockPart data = new MockPart("us", usRequestJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(
            multipart("/clubs/{club}/us", club.slug())
                .part(data)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createUs_badPayload() throws Exception {
    ClubInserted club = utilTest.insertClub();

    MockMultipartFile file1 =
        new MockMultipartFile(
            "files",
            "image1.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    String usRequestJson =
        """
                {
                  "textoIncorrecto": "json incorrecto"
                }
                """;

    MockPart data = new MockPart("us", usRequestJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    utilTest.mockUserWithClub(club.id());
    mockMvc
        .perform(
            multipart("/clubs/{club}/us", club.slug())
                .part(data)
                .file(file1)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().is4xxClientError());
  }
}
