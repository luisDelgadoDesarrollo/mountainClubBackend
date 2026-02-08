package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateActivityTest {

  @Autowired private UtilTest utilTest;

  @Autowired private MockMvc mockMvc;

  @AfterAll
  static void afterAll() throws IOException {
    Path dir = Paths.get("D:/Proyectos/ClubMontaña/backend/data/test/images");

    if (Files.exists(dir)) {
      // Borrar primero los archivos hijos
      Files.walk(dir)
          .sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  throw new RuntimeException("Error borrando " + path, e);
                }
              });

      System.out.println("🧹 Carpeta de imágenes de test borrada");
    } else {
      System.out.println("ℹ️ La carpeta no existe, nada que borrar");
    }
  }

  @Test
  void createActivity_happyPath_returns201() throws Exception {

    Long clubId = utilTest.insertClub();
    String json =
        """
                   {
                     "title": "Ruta guiada al Pico del Águila",
                     "description": "Actividad de montaña de nivel medio con guía titulado.",
                     "affiliatePrice": 25.00,
                     "noAffiliatePrice": 40.00,
                     "startDate": "2026-03-15T08:30:00",
                     "endDate": "2026-03-15T17:00:00",
                     "images": [
                       {
                         "image": "image-1",
                         "description": "Salida desde el refugio"
                       }
                     ]
                   }
                    """;

    MockPart data = new MockPart("activity", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image =
        new MockMultipartFile(
            "image-1",
            "photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(clubId);
    mockMvc
        .perform(multipart("/clubs/{clubId}/activities", clubId).part(data).file(image))
        .andExpect(status().isCreated());
  }

  @Test
  void createPublication_withoutAuthentication_returns401() throws Exception {
    Long clubId = utilTest.insertClub();
    MockPart data = new MockPart("data", "{}".getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/clubs/{clubId}/activities", clubId).part(data))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createPublication_invalidPayload_returns400() throws Exception {
    Long clubId = utilTest.insertClub();
    String invalidJson =
        """
                    {
                      "text": "sin título"
                    }
                    """;

    MockPart data = new MockPart("data", invalidJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            "user-1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    mockMvc
        .perform(
            multipart("/clubs/{clubId}/activities", clubId)
                .part(data)
                .with(authentication(authentication)))
        .andExpect(status().isBadRequest());
  }
}
