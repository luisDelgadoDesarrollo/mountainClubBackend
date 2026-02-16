package luis.delgado.clubmontana.backend.end2end.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;
import luis.delgado.clubmontana.backend.domain.model.enums.Sex;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.UserEntity;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.jpa.UserEntityJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserEntityJpa userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private ClubEntityJpa clubRepository;

  @Test
  void login_returns_201_and_token_when_credentials_are_valid() throws Exception {
    // given
    ClubEntity club = validClub();
    clubRepository.save(club);

    UserEntity user = validUser(club, passwordEncoder.encode("passwordSeguro123"));
    user.setEmail("socio@clubmontana.es");
    userRepository.save(user);

    String body =
        """
        {
          "username": "socio@clubmontana.es",
          "password": "passwordSeguro123"
        }
        """;

    // when / then
    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.expiresIn").isNumber());
  }

  @Test
  void login_returns_401_when_user_does_not_exist() throws Exception {
    String body =
        """
        {
          "username": "noexiste@clubmontana.es",
          "password": "password"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void login_returns_401_when_password_is_incorrect() throws Exception {
    ClubEntity club = validClub();
    clubRepository.save(club);

    UserEntity user = validUser(club, passwordEncoder.encode("passwordSeguro123"));

    userRepository.save(user);

    String body =
        """
        {
          "username": "socio@clubmontana.es",
          "password": "passwordIncorrecta"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void login_returns_401_when_email_is_not_verified() throws Exception {
    ClubEntity club = validClub();
    clubRepository.save(club);

    UserEntity user = unverifiedUser(club, passwordEncoder.encode("passwordSeguro123"));

    userRepository.save(user);

    String body =
        """
        {
          "username": "socio@clubmontana.es",
          "password": "passwordSeguro123"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void login_returns_400_when_request_is_invalid() throws Exception {
    String body =
        """
        {
          "username": "",
          "password": ""
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().is4xxClientError());
  }

  private ClubEntity validClub() {
    String suffix = UUID.randomUUID().toString().substring(0, 8);

    return ClubEntity.builder()
        .name("Club Montaña " + suffix)
        .slug("club-montana-" + suffix)
        .nif("G" + suffix)
        .url("club-" + suffix + ".es")
        .description("Club de prueba")
        .logo("logo.png")
        .hasInicio(true)
        .hasSecciones(true)
        .hasGaleria(true)
        .hasEnlaces(true)
        .hasContacto(true)
        .hasFederarse(true)
        .hasTienda(true)
        .hasCalendario(true)
        .hasConocenos(true)
        .hasNoticias(true)
        .hasForo(true)
        .hasEstatutos(true)
        .hasNormas(true)
        .hasHazteSocio(true)
        .contactEmail("delgadofernandez.luis@gmail.com")
        .phone("+34600123123")
        .build();
  }

  private UserEntity validUser(ClubEntity club, String encodedPassword) {
    String suffix = UUID.randomUUID().toString().substring(0, 8);

    return UserEntity.builder()
        .club(club)
        .email("user_" + suffix + "@clubmontana.es")
        .password(encodedPassword) // puedes meter uno real si quieres
        .dni("DNI" + suffix)
        .firstName("Luis")
        .lastName("Delgado")
        .birthDate(LocalDate.of(1990, 1, 1))
        .sex(Sex.M)
        .phone("600123123")
        .address("Calle Test 123")
        .city("Murcia")
        .postalCode("30001")
        .country("España")
        .emailVerified(true)
        .build();
  }

  /** Variante útil para tests negativos */
  private UserEntity unverifiedUser(ClubEntity club, String encodedPassword) {
    UserEntity user = validUser(club, encodedPassword);
    user.setEmailVerified(false);
    return user;
  }
}
