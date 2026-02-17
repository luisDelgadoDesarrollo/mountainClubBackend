package luis.delgado.clubmontana.backend.end2end.activities;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import luis.delgado.clubmontana.backend.end2end.AbstractWebIntegrationTest;
import luis.delgado.clubmontana.backend.end2end.ClubInserted;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UpdateActivityTest extends AbstractWebIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ActivityEntityJpa activityEntityJpa;

  @Test
  void updateActivity_happyPath_returns201() throws Exception {

    ClubInserted club = utilTest.insertClub();

    Pair<Long, String> activity = utilTest.createActivity(club);

    String json =
        """
                     {
                     "title": "Ruta guiada al Pico del Águila 2",
                     "description": "Actividad de montaña de nivel medio con guía titulado.",
                     "affiliatePrice": 30.00,
                     "noAffiliatePrice": 50.00,
                     "startDate": "2026-04-15T08:30:00",
                     "endDate": "2026-05-15T17:00:00",
                     "images": [
                       {
                         "image": "image-1",
                         "description": "Salida desde el refugio"
                       }
                     ]
                   }
                    """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "image-1",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(club.id());

    // when / then
    MvcResult mvcResult =
        mockMvc
            .perform(
                multipart(
                        HttpMethod.PUT,
                        "/clubs/{club}/activities/{activity}",
                        club.slug(),
                        activity.getSecond())
                    .part(data)
                    .file(image))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();
    Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    Optional<ActivityEntity> activityOptional = activityEntityJpa.findById(Long.valueOf(id));

    assertThat(activityOptional).isPresent();

    ActivityEntity activityGetted = activityOptional.get();

    assertThat(activityGetted.getNoAffiliatePrice()).isEqualByComparingTo("50.00");
    assertThat(activityGetted.getAffiliatePrice()).isEqualByComparingTo("30.00");
    assertThat(activityGetted.getStartDate()).isEqualTo("2026-04-15T08:30:00");
    assertThat(activityGetted.getEndDate()).isEqualTo("2026-05-15T17:00:00");
  }

  @Test
  void updateActivity_badDate() throws Exception {

    ClubInserted club = utilTest.insertClub();

    Pair<Long, String> activity = utilTest.createActivity(club);

    String json =
        """
                         {
                         "title": "Ruta guiada al Pico del Águila 2",
                         "description": "Actividad de montaña de nivel medio con guía titulado.",
                         "affiliatePrice": 30.00,
                         "noAffiliatePrice": 50.00,
                         "startDate": "2026-04-15T08:30:00",
                         "endDate": "2026-03-15T17:00:00",
                         "images": [
                           {
                             "image": "image-1",
                             "description": "Salida desde el refugio"
                           }
                         ]
                       }
                        """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "image-1",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(club.id());

    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/activities/{activity}",
                    club.slug(),
                    activity.getSecond())
                .part(data)
                .file(image))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateActivity_withoutAuthentication_returns401() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);

    String json =
        """
                         {
                         "title": "Ruta guiada al Pico del Águila 2",
                         "description": "Actividad de montaña de nivel medio con guía titulado.",
                         "affiliatePrice": 30.00,
                         "noAffiliatePrice": 50.00,
                         "startDate": "2026-04-15T08:30:00",
                         "endDate": "2026-05-15T17:00:00",
                         "images": [
                           {
                             "image": "image-1",
                             "description": "Salida desde el refugio"
                           }
                         ]
                       }
                        """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "image-1",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/activities/{activity}",
                    club.id(),
                    activity.getSecond())
                .part(data)
                .file(image)
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void updateActivity_invalidPayload_returns400() throws Exception {
    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity = utilTest.createActivity(club);
    utilTest.mockUserWithClub(club.id());
    String json =
        """
                             {
                             "test": "json erroneo",
                           }
                            """;

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JPEG mínimo válido para Apache Tika
    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "image-1",
            "image/jpeg",
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    mockMvc
        .perform(
            multipart(
                    HttpMethod.PUT,
                    "/clubs/{club}/activities/{activity}",
                    club.slug(),
                    activity.getSecond())
                .part(data)
                .file(image))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void updateActivity_happyPath_noAffiliatePrice_and_noEndDate_returns201() throws Exception {

    ClubInserted club = utilTest.insertClub();
    Pair<Long, String> activity1 = utilTest.createActivity(club);
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

    MockPart data = new MockPart("data", json.getBytes(StandardCharsets.UTF_8));
    data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    MockMultipartFile image =
        new MockMultipartFile(
            "files",
            "image-1",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});

    utilTest.mockUserWithClub(club.id());
    MvcResult mvcResult =
        mockMvc
            .perform(
                multipart(
                        HttpMethod.PUT,
                        "/clubs/{club}/activities/{activity}",
                        club.slug(),
                        activity1.getSecond())
                    .part(data)
                    .file(image))
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
