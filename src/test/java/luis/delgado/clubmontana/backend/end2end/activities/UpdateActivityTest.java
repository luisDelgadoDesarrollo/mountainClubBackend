package luis.delgado.clubmontana.backend.end2end.activities;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import luis.delgado.clubmontana.backend.end2end.UtilTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UpdateActivityTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityRepository activityRepository;
  @Autowired private UtilTest utilTest;

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
  void updateActivity_happyPath_returns201() throws Exception {

    Long clubId = utilTest.insertClub();

    Activity a1 = new Activity();
    a1.setClubId(clubId);
    a1.setTitle("Pub 1");
    a1.setDescription("Texto 1");
    a1.setImages(List.of());
    a1.setAffiliatePrice(BigDecimal.ONE);
    a1.setNoAffiliatePrice(BigDecimal.ONE);
    a1.setStartDate(LocalDateTime.now());
    a1.setEndDate(LocalDateTime.now());

    Activity saved = activityRepository.saveActivity(a1);
    Long activityId = saved.getActivityId();

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

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "image-1",
            "photo.jpg",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(clubId);

    // when / then
    mockMvc
        .perform(
            multipart(HttpMethod.PUT, "/clubs/{clubId}/activities/{activityId}", clubId, activityId)
                .part(data)
                .file(image))
        .andExpect(status().isCreated());
  }
}
