package luis.delgado.clubmontana.backend.end2end.activities;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateActivityTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityEntityJpa activityEntityJpa;

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
    MvcResult mvcResult =
        mockMvc
            .perform(multipart("/clubs/{clubId}/activities", clubId).part(data).file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    Optional<ActivityEntity> activityOptional = activityEntityJpa.findById(Long.valueOf(id));

    assertThat(activityOptional).isPresent();

    ActivityEntity activity = activityOptional.get();

    assertThat(activity.getNoAffiliatePrice()).isEqualByComparingTo("40.00");
    assertThat(activity.getAffiliatePrice()).isEqualByComparingTo("25.00");
    assertThat(activity.getStartDate()).isEqualTo("2026-03-15T08:30:00");
    assertThat(activity.getEndDate()).isEqualTo("2026-03-15T17:00:00");
  }

  @Test
  void createActivity_badDate() throws Exception {

    Long clubId = utilTest.insertClub();
    String json =
        """
                       {
                         "title": "Ruta guiada al Pico del Águila",
                         "description": "Actividad de montaña de nivel medio con guía titulado.",
                         "affiliatePrice": 25.00,
                         "noAffiliatePrice": 40.00,
                         "startDate": "2026-03-15T08:30:00",
                         "endDate": "2026-02-15T17:00:00",
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
        .andExpect(status().isBadRequest());
  }

  @Test
  void createActivity_withoutAuthentication_returns401() throws Exception {
    Long clubId = utilTest.insertClub();
    MockPart data =
        new MockPart(
            "data",
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
"""
                .getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(multipart("/clubs/{clubId}/activities", clubId).part(data))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void createActivity_invalidPayload_returns400() throws Exception {
    Long clubId = utilTest.insertClub();
    String invalidJson =
        """
                    {
                      "text": "sin título"
                    }
                    """;

    MockPart data = new MockPart("data", invalidJson.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    utilTest.mockUserWithClub(clubId);

    mockMvc
        .perform(multipart("/clubs/{clubId}/activities", clubId).part(data))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createActivity_happyPath_noAffiliatePrice_and_noEndDate_returns201() throws Exception {

    Long clubId = utilTest.insertClub();
    String json =
        """
                       {
                         "title": "Ruta guiada al Pico del Águila",
                         "description": "Actividad de montaña de nivel medio con guía titulado.",
                         "affiliatePrice": 25.00,
                         "startDate": "2026-03-15T08:30:00",
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
    MvcResult mvcResult =
        mockMvc
            .perform(multipart("/clubs/{clubId}/activities", clubId).part(data).file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

    Optional<ActivityEntity> activityOptional = activityEntityJpa.findById(Long.valueOf(id));

    assertThat(activityOptional).isPresent();
    ActivityEntity activity = activityOptional.get();

    assertThat(activity.getNoAffiliatePrice()).isEqualByComparingTo(activity.getAffiliatePrice());
    assertThat(activity.getEndDate()).isEqualTo(activity.getStartDate());
  }
}
